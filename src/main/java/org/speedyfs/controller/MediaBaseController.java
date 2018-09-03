package org.speedyfs.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.speedyfs.constants.AppConstants;
import org.speedyfs.enums.MimeMap;
import org.speedyfs.enums.MimeMap.MimeEnum;
import org.speedyfs.exception.ImageDataException;
import org.speedyfs.exception.MediaException;
import org.speedyfs.model.Content;
import org.speedyfs.model.FileData;
import org.speedyfs.model.ThumbFile;
import org.speedyfs.service.AlbumService;
import org.speedyfs.service.DeviceService;
import org.speedyfs.service.MediaService;
import org.speedyfs.utils.ThumbnailUtility;
import org.speedyfs.utils.UploadContentQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * Base controller for {@link MediaController}.
 * </p>
 * 
 * @author rahul.sharma3
 *
 */
public class MediaBaseController extends BaseController {
	private Log log = LogFactory.getLog(this.getClass());
	@Autowired
	MediaService mediaService;
	@Autowired
	AlbumService albumService;
	@Autowired
	TaskExecutor taskExecutor;
	@Autowired
	DeviceService deviceService;

	protected void runUploadQueue() {
		LinkedList<Content> contentList = null;
		try {
			contentList = UploadContentQueue.getInstance().popContentQueue();
			for (Iterator<Content> ite = contentList.iterator(); ite.hasNext();) {
				Content content = ite.next();
				String filePath = saveOriginalImage(content.getAlbumPath(), content.getInputFile());
				updateContentFlag(content.getClientImageData(), filePath);
				prepareImageData(content.getClientImageData(), content.getAlbumName(), filePath,
						content.getDirectoryId());
				createThumbAsync(filePath.substring(filePath.lastIndexOf(File.separator) + 1, filePath.length()),
						content.getAlbumPath(), content.getClientImageData());				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

	/**
	 * <p>
	 * Helper method to set the content flag according to the mimetype
	 * 
	 * @param clientImageData
	 *            file related data from client
	 * @param filePath
	 *            absolute path of file on server
	 * @throws IOException
	 */
	protected void updateContentFlag(FileData clientImageData, String filePath) throws IOException {
		Path path = Paths.get(filePath);
		String mimetype;
		try {
			mimetype = Files.probeContentType(path);
		} catch (IOException e) {
			e.printStackTrace();
			throw new MediaException("could not read the file mimetype.");
		}
		MimeEnum mimeEnum = MimeMap.getMimeMap().getFileType(mimetype);
		int media_type = clientImageData.getMedia_type();

		if (mimeEnum.getCode() == -1) {
			Files.deleteIfExists(path);
			log.info("method updateContentFlag Content not supported: " + mimeEnum);
			throw new ImageDataException("Content not supported.");
		} else {
			media_type = mimeEnum.getCode();
		}
		clientImageData.setMedia_type(media_type);
	}

	/**
	 * <p>
	 * Helper method to prepare the ImageData object to be saved in database
	 * 
	 * @param clientImageData
	 *            Image data from client
	 * @param albumName
	 *            album name of the content
	 * @param absolute
	 *            path of the original image on server
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 */
	protected void prepareImageData(FileData clientImageData, String albumName, String imagePath,
			int directoryId) throws IOException, NoSuchAlgorithmException {
		MessageDigest m = MessageDigest.getInstance("MD5");
		m.update(Files.readAllBytes(Paths.get(imagePath)));
		clientImageData.setFile_path(imagePath);
		clientImageData.setAlbumName(albumName);		
		clientImageData.setName(imagePath.substring(imagePath.lastIndexOf(File.separator) + 1, imagePath.length()));
		clientImageData.setSize(updateFileSize(getFilePath(clientImageData)));
		/*if (StringUtils.isEmpty(clientImageData.getFile_id())) {
			clientImageData.setFile_id(clientImageData.getImage_hash() + "-" + String.valueOf(directoryId));
		} else {// Quick fix to diffrentiate local and web images in mac application
			clientImageData.setFile_id(clientImageData.getFile_id() + String.valueOf(directoryId));
		}*/
	}

	/**
	 * <p>
	 * Takes the parameters and generates the Image thumbnails by using
	 * TaskExecutor.
	 * 
	 * @param imgName
	 *            Name of the image.
	 * @param albumPath
	 *            Album absolute path.
	 * @param clientImageData
	 *            {@link FileData} Image data submitted by client application
	 */
	protected void createThumbAsync(String imgName, String albumPath, FileData clientImageData) {
		taskExecutor.execute(() -> {
			try {
				mediaService.saveImageData(clientImageData);				
				ThumbFile thumbFile = null;
				ThumbnailUtility thumbnailUtility = new ThumbnailUtility();
				if ((clientImageData.getMedia_type() & (1 << 2)) == (1 << 2)) {// (1<<2) is 100 which is 4 as a int
					thumbnailUtility.generateVideoThumbAsync(albumPath, imgName);// Generate video thumb to generate
																					// different resolution thumbs using
																					// image thumb generator.
					thumbFile = thumbnailUtility.generateThumbAsync(
							albumPath + File.separator + AppConstants.THUMBNAIL_DIR_NAME,
							imgName.replace(".", AppConstants.UNDERSCORE) + ".png");
					thumbFile.setFile_id(clientImageData.getFile_id());
					
				} else if ((clientImageData.getMedia_type() & (1 << 4)) == (1 << 4)) {// (1<<4) is 10000 which is 16
																							// as a int
					thumbFile = new ThumbFile();
					thumbFile.setFile_id(clientImageData.getFile_id());
				} else if ((clientImageData.getMedia_type() & (1 << 5)) == (1 << 5)) {// (1<<5) is 100000 which is 32
																							// as a int
					thumbFile = new ThumbFile();
					thumbFile.setFile_id(clientImageData.getFile_id());
				} else {
					thumbFile = thumbnailUtility.generateThumbAsync(albumPath, imgName);
					thumbFile.setFile_id(clientImageData.getFile_id());
				}				
				mediaService.saveThumbnails(thumbFile);
				/**
				 * Call to generate hls files if uploaded file is video file
				 */				
				if ((clientImageData.getMedia_type() & (1 << 2)) == (1 << 2)) {
					
					new ThumbnailUtility().generateHlsFiles(String.valueOf(clientImageData.getFile_id()),
							getFilePath(clientImageData), albumPath);
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * <p>
	 * Write original image bytes from server file System to the response output
	 * Stream.
	 * 
	 * @param imgPath
	 *            Path of Image on Server
	 * @param response
	 *            <code>HttpServletResponse
	 * @throws IOException
	 */
	protected void readImage(String imgPath, HttpServletResponse response) throws IOException {
		byte[] byteArray = null;
		FileInputStream imgStream = null;
		FileChannel channel = null;
		ByteBuffer byteBuf = null;
		ServletOutputStream out = null;
		imgStream = new FileInputStream(imgPath);
		channel = imgStream.getChannel();
		byteBuf = channel.map(MapMode.READ_ONLY, 0, channel.size());
		byteArray = new byte[byteBuf.limit()];
		response.setHeader("Content-Length", String.valueOf(byteBuf.limit()));
		byteBuf.get(byteArray);
		out = response.getOutputStream();
		out.write(byteArray, 0, byteBuf.limit());
		byteBuf.clear();
		channel.close();
		imgStream.close();
		byteBuf = null;
		System.gc();
		out.flush();
		out.close();
		
	}

	/**
	 * <p>
	 * Create zip file of images and write the zip into the response output Stream.
	 * 
	 * @param images
	 *            Map of image names and corresponding image byte array
	 * @param response
	 *            Response object for the client
	 * @throws IOException
	 */
	protected void writeZip(Map<Long, byte[]> images, HttpServletResponse response) throws IOException {
		response.setContentType("application/zip");
		response.setHeader("Content-Disposition", "attachment; filename=thumbnails.zip");
		ServletOutputStream out = response.getOutputStream();
		ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(out));
		zos.setLevel(0);
		for (Long imgId : images.keySet()) {
			zos.putNextEntry(new ZipEntry(imgId.toString()));
			zos.write(images.get(imgId));
			zos.closeEntry();
		}
		zos.flush();
		zos.close();
	}

	/**
	 * <p>
	 * Save the original image on server's file system
	 * 
	 * @param albumPath
	 *            Absolute path to the album
	 * @param inputFile
	 *            Multipart File object to save file on server file system
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	protected String saveOriginalImage(String albumPath, MultipartFile inputFile)
			throws IllegalStateException, IOException {
		File album = new File(albumPath);
		if (!album.exists()) {
			album.mkdirs();
			ThumbnailUtility thumbnailUtility = new ThumbnailUtility();
			thumbnailUtility.createThumbDirectories(albumPath);
		}
		String fileName = inputFile.getOriginalFilename();
		String name = fileName.substring(0, fileName.lastIndexOf(AppConstants.DOT));
		String extn = fileName.substring(fileName.lastIndexOf(AppConstants.DOT) + 1, fileName.length());
		File imageFile = new File(new StringBuilder(album.getAbsolutePath()).append(File.separator).append(name)
				.append(AppConstants.UNDERSCORE).append(System.currentTimeMillis()).append(AppConstants.DOT)
				.append(extn).toString());
		inputFile.transferTo(imageFile);
		return imageFile.getAbsolutePath();
	}	
	
	/**
	 * Method returns size of file in bytes.
	 * 
	 * @param filePath
	 * @return
	 */
	protected long updateFileSize(String filePath) {
		File file = new File(filePath);
		return file.length();
	}

}
