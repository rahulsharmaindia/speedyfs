package org.speedyfs.utils;

import org.apache.commons.io.FilenameUtils;
import org.apache.tika.Tika;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author gauri.shukla
 *
 */
@Component
public class MimeTypeUtils {

	private static final Tika tika = new Tika();

	private static Map<String, String> MimeMap;
	static {
		MimeMap = new HashMap<>();
		MimeMap.put("mp4", "video/mp4");
		MimeMap.put("mp3", "audio/mp3");
		MimeMap.put("flv", "video/flv");
		MimeMap.put("webm", "video/webm");
		MimeMap.put("", "video/mp4");
	}

	public static String getMimeType(String extension) {
		if (extension.isEmpty())
			return "application/octet-stream";

		if (MimeMap.containsKey(extension)) {
			return MimeMap.get(extension);
		} else {
			return "unknown/" + extension;
		}
	}

	public static String probeContentType(Path file) throws IOException {
		String mimetype = Files.probeContentType(file);
		if (mimetype != null)
			return mimetype;

		mimetype = tika.detect(file.toFile());
		if (mimetype != null)
			return mimetype;

		return getMimeType(FilenameUtils.getExtension(String.valueOf(file.getFileName())));
	}

}
