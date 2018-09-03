package org.speedyfs.transformer;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import org.speedyfs.dto.ImageListDTO;

/**
 * <p>
 * Custom ObjectMapper register the custom serializer for DTOs
 * @author rahul.sharma3
 *
 */
public class CustomObjectMapper extends ObjectMapper {
	private static final long serialVersionUID = -2424369959978681362L;

	public CustomObjectMapper() {
		SimpleModule module = new SimpleModule("JSONModule", new Version(2, 0, 0, null, null, null));
		module.addSerializer(ImageListDTO.class, new ImageListDTOSerializer());
		registerModule(module);
	}
}
