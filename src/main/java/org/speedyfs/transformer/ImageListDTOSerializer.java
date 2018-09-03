package org.speedyfs.transformer;

import java.io.IOException;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import org.speedyfs.dto.ImageListDTO;
import org.speedyfs.model.FileData;

/**
 * <p>
 * Custom JSON serializer for {@code ImageListDTO}
 * @author rahul.sharma3
 *
 */
public class ImageListDTOSerializer extends StdSerializer<ImageListDTO> {

	public ImageListDTOSerializer() {
		super(ImageListDTO.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.fasterxml.jackson.databind.ser.std.StdSerializer#serialize(java.lang.Object, com.fasterxml.jackson.core.JsonGenerator,
	 * com.fasterxml.jackson.databind.SerializerProvider)
	 */
	@Override
	public void serialize(ImageListDTO value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		jgen.writeStartObject();
		jgen.writeStringField("latModified", value.getLastModified());
		jgen.writeStringField("path", value.getPath());
		jgen.writeArrayFieldStart("imageFile");
		for (FileData data : value.getImageFile()) {
			jgen.writeStartObject();
			jgen.writeStringField("fileId", String.valueOf(data.getFile_id()));
			jgen.writeStringField("fileName", data.getName());
			jgen.writeStringField("lastModified", sdf.format(data.getLastModified()));
			jgen.writeNumberField("size", data.getSize() == null ? 0L : data.getSize());
			jgen.writeNumberField("media_type", data.getMedia_type());
			jgen.writeNumberField("content_flags", data.getContent_flags());
			jgen.writeStringField("creation_date", data.getCreation_date());
			jgen.writeEndObject();
		}
		jgen.writeEndArray();
		jgen.writeEndObject();
	}
}
