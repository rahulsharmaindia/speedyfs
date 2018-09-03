package org.speedyfs.controller;

import java.util.Map;

import javax.activity.InvalidActivityException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.speedyfs.dto.AlbumDTO;
import org.speedyfs.dto.DirectoryListDTO;
import org.speedyfs.dto.ImageListDTO;
import org.speedyfs.exception.AlbumDataException;
import org.speedyfs.exception.AlbumException;
import org.speedyfs.model.Album;
import org.speedyfs.model.Device;
import org.speedyfs.service.AlbumService;
import org.speedyfs.service.DeviceService;
import org.speedyfs.utils.CommonUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * Album controller to
 * </p>
 * <p>
 * Create Album.
 * <p>
 * Get Album List.
 * <p>
 * Get album contents list.
 * <p>
 * Delete Album.
 * 
 * <p>
 * 
 * @author Rahul Sharma
 *
 */
@RestController
@RequestMapping(value = "/directories")
public class AlbumController extends AlbumBaseController {
	@Autowired
	DeviceService deviceService;
	@Autowired
	AlbumService albumService;
	private Log log = LogFactory.getLog(this.getClass());
	private String DEFAULT_DIRECTORY = "MyMedia"; 

	/**
	 * Create a directory on the server with the name provided in request URL.
	 * 
	 * @param directoryName
	 * @return ResponseEntity&lt;Map&lt;String, String&gt;&gt;
	 */
	@RequestMapping(value = "/create-directory", method = RequestMethod.POST)
	public ResponseEntity<Album> createAlbum(@RequestBody @Valid AlbumDTO albumDto, HttpServletRequest req) {
		String user = req.getHeader("user");		
		String actualDirectoryName = albumDto.getDirectoryName() + "~" + user;		
		albumDto.setDirectoryName(actualDirectoryName);
		Album album = albumService.saveAlbum(albumDto, user, getAlbumPath(albumDto.getDirectoryName()));
		album.setAlbumName(new CommonUtility().getVirtualAlbumName(album.getAlbumName()));
		return new ResponseEntity<Album>(album, HttpStatus.OK);
	}	

	/**
	 * To get the List of all albums created by logged in user and the public
	 * directory inside the root directory.
	 * 
	 * @param device
	 * @param req
	 *            {@link HttpServletRequest}
	 * @return ResponseEntity&lt;{@link DirectoryListDTO}&gt;
	 * @throws InvalidActivityException
	 */
	@RequestMapping(value = "/list-user-directories", method = RequestMethod.POST)
	public ResponseEntity<DirectoryListDTO> listUserAlbums(@RequestBody @Valid Device device, HttpServletRequest req)
			throws InvalidActivityException {
		String user = req.getHeader("user");
		deviceService.saveDevice(device);
		String serverName = (String) servletContext.getAttribute("serverName");
		return new ResponseEntity<DirectoryListDTO>(albumService.getUserAlbumList(serverName, user), HttpStatus.OK);
	}

	/**
	 * To get the List of all images inside the directory provided in request URL.
	 * 
	 * @param directoryId
	 *            Id of directory
	 * @return ResponseEntity&lt;{@link ImageListDTO}&gt;
	 */
	@RequestMapping(value = "/get-directory-contents/{directoryId}", method = RequestMethod.GET)
	public ResponseEntity<ImageListDTO> getAlbumContents(
			@NotNull @Valid @PathVariable("directoryId") Integer directoryId) {
		Album album = albumService.getAlbum(directoryId);
		if (album == null) {
			log.info("Album with albumId: " + directoryId + " doesn't exists.");
			throw new AlbumDataException("Album with albumId: " + directoryId + " doesn't exists.");
		}
		String albumName = album.getAlbumName();
		ImageListDTO imageListDTO = albumService.getAlbumFiles(albumName, getAlbumPath(albumName));
		if (!CollectionUtils.isEmpty(imageListDTO.getImageFile())) {
			return new ResponseEntity<ImageListDTO>(imageListDTO, HttpStatus.OK);
		} else {
			log.info("Album with albumId: " + directoryId + " doesn't contains any file.");
			throw new AlbumDataException("Album with albumId: " + directoryId + " doesn't contains any file.");
		}
	}

	/**
	 * To delete the a particular directory provided in request URL.
	 * 
	 * @param directoryId
	 *            id of directory
	 * @return ResponseEntity&lt;Map&lt;String, String&gt;&gt;
	 */
	@RequestMapping(value = "/delete-directory/{directoryId}", method = RequestMethod.DELETE)
	public ResponseEntity<AlbumDTO> deleteAlbum(@NotNull @Valid @PathVariable("directoryId") Integer directoryId,
			HttpServletRequest req) {
		String user = req.getHeader("user");
		Album album = albumService.getAlbum(directoryId);

		if (album != null) {

			if (album.getCreatedBy().equalsIgnoreCase(user)) {
				String privateAlbumName = DEFAULT_DIRECTORY + "~" + user;
				if (privateAlbumName.equalsIgnoreCase(album.getAlbumName())) {
					throw new AlbumException("User not authorized to delete Document Album.");
				}
				try {
					AlbumDTO albumDTO = albumService.deleteAlbum(album, getAlbumPath(album.getAlbumName()));
					if (albumDTO.getAccess() == 2) {
						albumService.deleteSharedAlbumsEntries(album.getAlbumId());
					}
					return new ResponseEntity<AlbumDTO>(albumDTO, HttpStatus.OK);
				} catch (Exception e) {
					log.info("Error occured in deleting Album with albumId: " + album.getAlbumId(), e);
					throw new AlbumException("Error occured while deleting albumId: " + album.getAlbumId());
				}

			} else {
				log.info("Delete operation failed, Logged-in User :" + album.getCreatedBy()
						+ " is not the Owner of the Album.");
				throw new AlbumException("Delete operation failed, Logged-in User :" + album.getCreatedBy()
						+ " is not the Owner of the Album.");
			}

		} else {
			log.info("Album with albumId: " + directoryId + " is not present on server");
			throw new AlbumException("Album with albumId: " + directoryId + " is not present on server");
		}
	}

	/**
	 * To modify access permission of particular directory provided in request URL.
	 * 
	 * @param directoryId
	 *            id of directory
	 * @param access
	 *            access permission, false for private, true for public.
	 * @return ResponseEntity&lt;Map&lt;String, String&gt;&gt;
	 */
	@RequestMapping(value = "/modify-directory-access", method = RequestMethod.PUT)
	public ResponseEntity<Map<String, String>> modifyAlbumAccess(@RequestBody @Valid AlbumDTO albumDto,
			HttpServletRequest req) {
		Album album = albumService.getAlbum(albumDto.getDirectoryId());
		String user = req.getHeader("user");
		if (album != null) {
			String privateAlbumName = DEFAULT_DIRECTORY + "~" + user;
			if (privateAlbumName.equalsIgnoreCase(album.getAlbumName())) {
				throw new AlbumException("User not authorized to change access permission of Document Album.");
			}
			boolean flag = false;
			if (album.getCreatedBy().equalsIgnoreCase(user)) {
				int access = album.getAccess();
				album.setAccess(albumDto.getAccess());
				albumService.modifyAlbumAccess(album);
				if (access == 2 && albumDto.getAccess() != 2) {
					flag = albumService.deleteSharedAlbumsEntries(album.getAlbumId());
				}

				return new ResponseEntity<Map<String, String>>(HttpStatus.OK);
			} else {
				log.info("User: " + user + " not authorized to change access permission of album:"
						+ album.getAlbumName());
				throw new AlbumException("User not authorized to change access permission.");
			}

		} else {
			log.info("Album ID:" + albumDto.getDirectoryId() + " is not present on server");
			throw new AlbumException("Album is not present on server");
		}
	}

	/**
	 * Create a default directory on the server with the name provided in request
	 * URL.
	 * 
	 * @param directoryName
	 * @return ResponseEntity&lt;Map&lt;String, String&gt;&gt;
	 */
	@RequestMapping(value = "/create-default-directory", method = RequestMethod.POST)
	public ResponseEntity<Album> createDafaultAlbum(@RequestBody @Valid Device device, HttpServletRequest req) {
		String user = req.getHeader("user");
		deviceService.saveDevice(device);
		String albumName = DEFAULT_DIRECTORY + "~" + user;
		Album album = albumService.getAlbum(albumName);
		if (null != album) {
			return new ResponseEntity<Album>(album, HttpStatus.OK);
		}
		AlbumDTO albumDto = new AlbumDTO();
		albumDto.setDirectoryName(albumName);
		album = albumService.getDefaultAlbum(albumDto, user, getAlbumPath(albumDto.getDirectoryName()));
		return new ResponseEntity<Album>(album, HttpStatus.OK);
	}
}
