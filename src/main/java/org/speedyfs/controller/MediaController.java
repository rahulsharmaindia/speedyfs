package org.speedyfs.controller;

import java.beans.PropertyEditorSupport;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.speedyfs.dto.ThumbBatchReqDTO;
import org.speedyfs.enums.Thumbnail;
import org.speedyfs.exception.ImageDataException;
import org.speedyfs.exception.MediaException;
import org.speedyfs.model.Album;
import org.speedyfs.model.Content;
import org.speedyfs.model.FileData;
import org.speedyfs.utils.UploadContentQueue;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author rahul.sharma3
 *
 */
@RestController
@RequestMapping(value = "/files")
public class MediaController extends MediaBaseController {
	private Log log = LogFactory.getLog(this.getClass());

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
		dataBinder.registerCustomEditor(ThumbBatchReqDTO.class, new PropertyEditorSupport() {
			Object value;

			@Override
			public Object getValue() {
				return value;
			}

			@Override
			public void setAsText(String text) throws IllegalArgumentException {
				ObjectMapper mapper = new ObjectMapper();
				try {
					value = mapper.readValue(text, ThumbBatchReqDTO.class);
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
	 * To save a image provided in request body and clientImage data into
	 * database.
	 * 
	 * @param directoryId
	 *            Id of directory to upload contents.
	 * @param inputFile
	 *            file object
	 * @param clientImageData
	 *            file related data from client
	 * @return ResponseEntity&lt;Map&lt;String, String&gt;&gt;
	 * @throws NoSuchAlgorithmException
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	@RequestMapping(value = "/upload-content/{directoryId}", method = RequestMethod.POST)
	public ResponseEntity<Map<String, String>> uploadContent(@PathVariable("directoryId") Integer directoryId, @RequestParam("file") MultipartFile inputFile,
			@RequestParam(value = "filedata", required = true) FileData clientImageData, HttpServletRequest req) throws NoSuchAlgorithmException, IllegalStateException, IOException {

		if (clientImageData != null) {
			if (clientImageData.getMedia_type() == null) {
				clientImageData.setMedia_type(0);
			}	
			clientImageData.setCreatedBy(req.getHeader("user"));
			Album album = albumService.getAlbum(directoryId);
			String albumPath = getAlbumPath(album.getAlbumName());// album.getAlbumPath();
			/*deviceService.updateDevice(clientImageData.getDevice());*/
			Content content = new Content(directoryId, inputFile, clientImageData, album.getAlbumName(), albumPath);

			try {
				UploadContentQueue.getInstance().uploadContent(content);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (UploadContentQueue.getInstance().isReadytoRun()) {
				runUploadQueue();
			}
		} else {
			log.info("service upload-content: ClientImageData is null ");
			throw new MediaException("Error occurred in uploading file.");
		}
		return new ResponseEntity<Map<String, String>>(HttpStatus.OK);
	}

	/**
	 * <p>
	 * Returns the byte array of thumbnails of imageIds provided in request
	 * parameter.
	 * 
	 * @param thumb
	 *            thumbnail size requested by the client
	 * @param thumbBatchReqDTO
	 *            object containing image ids to be downloaded
	 * @param response
	 *            Response object of client
	 * @return ResponseEntity&lt;byte[]&gt;
	 */
	@RequestMapping(value = "/get-file-thumbnails", method = RequestMethod.POST)
	public ResponseEntity<byte[]> getFileThumbnails(@RequestParam(value = "thumb", required = true) String thumb,
			@RequestParam(value = "imageIds", required = true) ThumbBatchReqDTO thumbBatchReqDTO, HttpServletResponse response) {
		if (!StringUtils.isEmpty(thumb)) {
			try {
				Map<Long, byte[]> images = mediaService.loadImages(thumbBatchReqDTO, Thumbnail.valueOf(thumb));
				if (CollectionUtils.isEmpty(images)) {
					throw new ImageDataException();
				}
				writeZip(images, response);
				return new ResponseEntity<byte[]>(HttpStatus.OK);
			} catch (Exception e) {
				log.info("Exception in get-file-thumbnails:", e);
				throw new MediaException("Problem occurred in retrieving zipped thumbnils.");
			}
		}
		throw new MediaException();
	}

	/**
	 * <p>
	 * To get the image stream of a particular content or content thumbnail
	 * provided in request URL.
	 * 
	 * @param contentId
	 *            Id of the content to be downloaded
	 * @param thumb
	 *            thumbnail size requested
	 * @param response
	 *            Response object of client
	 * @return ResponseEntity&lt;byte[]&gt;
	 * @throws IOException
	 */
	@RequestMapping(value = "/get-content/{contentId}", method = RequestMethod.GET) // {albumName}/{imgName:.+}
	public ResponseEntity<byte[]> getContent(@PathVariable("contentId") String contentId, @RequestParam(value = "thumb", required = false) String thumb,
			HttpServletResponse response) throws IOException {
		Long fileId = Long.parseLong(contentId);
		if (!StringUtils.isEmpty(thumb)) {
			byte[] thumbBytes = mediaService.loadThumbnail(fileId, Thumbnail.valueOf(thumb));
			ServletOutputStream out = response.getOutputStream();
			out.write(thumbBytes);
			out.flush();
			out.close();
		} else {
			FileData imageData = mediaService.loadImageData(fileId);			
			String imagePath = getFilePath(imageData);/*imageData.getFile_path();*/
			readImage(imagePath, response);
		}
		return new ResponseEntity<byte[]>(HttpStatus.OK);
	}

	/**
	 * To delete the particular content by contentId provided in request URL.
	 * 
	 * @param contentId
	 *            Id of the content to be deleted.
	 * @return ResponseEntity&lt;Map&lt;String, String&gt;&gt;
	 */
	@RequestMapping(value = "/delete-content/{contentId}", method = RequestMethod.DELETE)
	public ResponseEntity<Map<String, String>> deleteContent(@PathVariable("contentId") String contentId, HttpServletRequest req) {
		String user = req.getHeader("user");
		HashMap<String, String> responseMap = new HashMap<String, String>();
		Long fileId = Long.parseLong(contentId);
		boolean deleted = mediaService.deleteImage(fileId, user);
		if (deleted) {
			responseMap.put("Message", "File deleted successfully");
			return new ResponseEntity<Map<String, String>>(responseMap, HttpStatus.OK);
		} else {
			throw new MediaException("Can't delete file");
		}
	}

	/**
	 * Save the content data provided by mac user into dartabase on server.
	 * 
	 * @param obj
	 *            ClientImageData updated file related data from client
	 * @return ResponseEntity&lt;String&gt;
	 */
	@RequestMapping(value = "/upload-content-info", method = RequestMethod.POST)
	public ResponseEntity<String> uploadContentInfo(@RequestBody FileData obj) {
		try {
			mediaService.updateImageData(obj);
		} catch (Exception e) {
			log.info("Exception in upload-content-info:", e);
			throw new MediaException("Failed to update File Data.");
		}
		return new ResponseEntity<String>("File Data updated successfully.", HttpStatus.OK);
	}

	/**
	 * @param contentId
	 *            Id of the content to be updated.
	 * @return ResponseEntity&lt;{@link FileData}&gt;
	 */
	@RequestMapping(value = { "/get-content-comments/{contentId}", "/get-content-comments" }, method = { RequestMethod.GET, RequestMethod.POST })
	public ResponseEntity<FileData> uploadContentInfo(@PathVariable("contentId") Optional<String> contentId) {
		if (contentId.isPresent()) {
			Long fileId = Long.parseLong(contentId.get());
			FileData clientImageData = mediaService.getContentComments(fileId);
			return new ResponseEntity<FileData>(clientImageData, HttpStatus.OK);
		} else {
			return new ResponseEntity<FileData>(HttpStatus.PRECONDITION_REQUIRED);
		}
	}
}
