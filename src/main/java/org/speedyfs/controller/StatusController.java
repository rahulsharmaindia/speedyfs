package org.speedyfs.controller;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.speedyfs.dto.AlbumListDTO;
import org.speedyfs.model.Album;
import org.speedyfs.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p> Controller to check the different status related to directory and server.
 * 
 * @author rahul.sharma3
 *
 */
@RestController
@RequestMapping(value = "/status")
public class StatusController extends BaseController {

	@Autowired
	AlbumService albumService;
	
	/**
	 * <p>
	 * Used to get the last modified timeStamp of all the directories provided
	 *
	 * @param obj
	 *            {@link AlbumListDTO} List of albums whose status need to be checked
	 * @return ResponseEntity&lt;Map&lt;String, String&gt;&gt;
	 */
	@RequestMapping(value = "/check-directory-status", method = RequestMethod.POST)
	public ResponseEntity<Map<Integer, String>> checkStatus(@RequestBody AlbumListDTO obj) {
		HashMap<Integer, String> map = new HashMap<>(obj.getIds().size());
		for (Integer dirId : obj.getIds()) {
			Album album = albumService.getAlbum(dirId);
			if (album != null) {
				File file = new File(
						((String) servletContext.getAttribute("rootPath")) + File.separator + album.getAlbumName());
				if (!file.exists()) {
					map.put(album.getAlbumId(), "0");
				} else {
					map.put(album.getAlbumId(), Long.toString(file.lastModified()));
				}
			}
			
		}
		return new ResponseEntity<Map<Integer, String>>(map, HttpStatus.OK);
	}

	/**
	 * <p>
	 * Used to check the server status, whether server is responding or not.
	 *
	 * @return ResponseEntity&lt;String&gt;
	 */
	@RequestMapping(value = "/ping", method = RequestMethod.GET)
	public ResponseEntity<String> checkStatus() {
		return new ResponseEntity<String>(HttpStatus.OK);
	}
}
