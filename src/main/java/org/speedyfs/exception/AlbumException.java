package org.speedyfs.exception;

public class AlbumException extends RuntimeException {
	private static final long serialVersionUID = 430975643157192858L;

	public AlbumException() {
		super();
	}

	public AlbumException(String Message) {
		super(Message);
	}
}
