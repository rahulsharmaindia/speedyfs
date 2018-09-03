package org.speedyfs.dto;

public class ResponseDTO {
	 
	private int status;
 
    private String message;
    
	private ErrorDTO errors;

	private Object contents;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public ErrorDTO getErrors() {
		return errors;
	}

	public void setErrors(ErrorDTO errors) {
		this.errors = errors;
	}

	public Object getContents() {
		return contents;
	}

	public void setContents(Object contents) {
		this.contents = contents;
	}
}
