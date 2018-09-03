package org.speedyfs.dto;

import java.util.ArrayList;
import java.util.List;

public class ErrorDTO {

	private List<FieldErrorDTO> fieldErrors = new ArrayList<>();

	public List<FieldErrorDTO> getFieldErrors() {
		return fieldErrors;
	}

	public void setFieldErrors(List<FieldErrorDTO> fieldErrors) {
		this.fieldErrors = fieldErrors;
	}

}