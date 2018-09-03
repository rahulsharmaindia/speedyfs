package org.speedyfs.dto;

import org.speedyfs.enums.MessageType;

public class FieldErrorDTO {
	 
    private String field;
 
    private String message;
    
    private MessageType messageType;

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public MessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}
 
	@Override
	public String toString() {
		return "FIELD: " + field + " MESSAGE: " + message + " MESSAGE TYPE: " + messageType;
	}
    
}
