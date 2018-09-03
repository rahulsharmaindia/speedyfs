package org.speedyfs.controller;

import java.beans.PropertyEditorSupport;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.speedyfs.constants.AppConstants;
import org.speedyfs.dto.VideoFileDTO;
import org.speedyfs.exception.VideoException;
import org.speedyfs.model.FileData;
import org.speedyfs.utils.MultipartFileSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Gauri.Shukla
 *
 */
@RestController
@RequestMapping(value = "/videos")
public class VideoController extends MediaBaseController {
	private Log log = LogFactory.getLog(this.getClass());	
	@Value("#{'${videos.folder}'}")
	private String videoFolderName;
	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		dataBinder.registerCustomEditor(FileData.class, new PropertyEditorSupport() {
			Object value;

			@Override
			public Object getValue() {
				return value;
			}

			@Override
			public void setAsText(String text) throws IllegalArgumentException {
				ObjectMapper mapper = new ObjectMapper();
				try {
					value = mapper.readValue(text, FileData.class);
				} catch (JsonParseException e) {
					e.printStackTrace();
				} catch (JsonMappingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * <p>
	 * To get the video stream of a particular content. in request URL.
	 * 
	 * @param contentId
	 *            Id of the content to be downloaded
	 * @param response
	 *            Response object of client
	 * @return ResponseEntity&lt;byte[]&gt;
	 * @throws IOException
	 */
	@RequestMapping(value = "/get-content/{contentId}", method = RequestMethod.GET) // {albumName}/{imgName:.+}
	public ResponseEntity<byte[]> getContent(@PathVariable("contentId") String contentId, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		Long fileId = Long.parseLong(contentId);
		FileData imageData = mediaService.loadImageData(fileId);
 		String size = (imageData == null || imageData.getSize() == null) ? "0" : imageData.getSize().toString();
		response.setHeader("videoSize", size);
		String imagePath = getFilePath(imageData);/*imageData.getFile_path();*/
		Path path = FileSystems.getDefault().getPath(imagePath);
		try {
			MultipartFileSender.fromPath(path).with(request).with(response).serveResource();
		} catch (Exception e) {
			log.info("Error occured in video streaming.", e);
			throw new VideoException("Error occured in video streaming.");			
		}

		return new ResponseEntity<byte[]>(HttpStatus.OK);
	}

	/**
	 * <p>
	 * To get the video stream of a particular content. in request URL.
	 * 
	 * @param contentId
	 *            Id of the content to be downloaded
	 * @param response
	 *            Response object of client
	 * @return ResponseEntity&lt;byte[]&gt;
	 * @throws IOException
	 */
	@RequestMapping(value = "/hls-content/{contentId}", method = RequestMethod.GET)
	public ResponseEntity<VideoFileDTO> getHlsContent(@PathVariable("contentId") String contentId,
			HttpServletRequest request, HttpServletResponse response) throws IOException {

		VideoFileDTO videoFileDTO = new VideoFileDTO();
		videoFileDTO.setUrl("");
		videoFileDTO.setStatus("false");
		videoFileDTO.setResponseMsg("Video file doesn't Exists.");

		if (videoFolderName.isEmpty()) {
			log.info("Folder/directory name of video is not configured.");
			return new ResponseEntity<VideoFileDTO>(videoFileDTO, HttpStatus.OK);
		}
		Long fileId = Long.parseLong(contentId);
		FileData imageData = mediaService.loadImageData(fileId);
		Long photoId = imageData.getFile_id();
		String imagePath = getFilePath(imageData);/*imageData.getFile_path();*/
		String albumPath = imagePath.substring(0, imagePath.lastIndexOf(File.separator));
		String hlsDirectoryPath = albumPath + File.separator + photoId;
		
		//String imagePath = imageData.getFile_path();
		//File videoFile = new File(imagePath);
		File resourceDirectory = new File(getRootPath());
		//imagePath = videoFile.getAbsolutePath();
		String resourcePath = resourceDirectory.getAbsolutePath();
		if (!isM3u8Exists(hlsDirectoryPath)) {
			log.info("m3u8 file required doesn't exists.");
			return new ResponseEntity<VideoFileDTO>(videoFileDTO, HttpStatus.OK);
		}
		
		String relPath = getRelativeFilePath(imagePath, resourcePath, imageData.getFile_id()/*, fileNameNoExt*/);
		URL serverURL = new URL(request.getScheme(), // http
				request.getServerName(), // host
				request.getServerPort(), // port
				relPath);
		videoFileDTO.setUrl(serverURL.toString());
		videoFileDTO.setStatus("true");
		videoFileDTO.setResponseMsg("");
		return new ResponseEntity<VideoFileDTO>(videoFileDTO, HttpStatus.OK);
	}
	
	/**
	 * Method to check m3u8 file exists or not.
	 * 
	 * @param imagePath
	 * @param fileNameNoExt
	 * @return
	 */	
	
	private boolean isM3u8Exists(String hlsDirectoryPath) {
		String m3u8Path = hlsDirectoryPath + File.separator
				+ AppConstants.HLS_FILE_NAME;
		File m3u8File = new File(m3u8Path);
		return m3u8File.exists();
	}

	/**
	 * Method to fetch relative path of video files.
	 * 
	 * @param imagePath
	 * @param resourcePath
	 * @param fileNameNoExt
	 * @return
	 */
	
	private String getRelativeFilePath(String imagePath, String resourcePath, Long photoId/*, String fileNameNoExt*/) {
		File imageFile = new File(imagePath);
		imagePath = imageFile.getParentFile().getAbsolutePath();
		String relPath = imagePath.substring(imagePath.lastIndexOf(resourcePath) + resourcePath.length());
		relPath = relPath.replaceAll("\\\\", "");
		relPath = relPath.replaceAll("\\/", "");
		relPath = AppConstants.FORWARD_SLASH + videoFolderName + AppConstants.FORWARD_SLASH
					+ relPath + AppConstants.FORWARD_SLASH + photoId
					+ AppConstants.FORWARD_SLASH + AppConstants.HLS_FILE_NAME;		
		return relPath;
	}
}
