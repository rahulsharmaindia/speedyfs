package org.speedyfs.exception;

public class AlbumDataException extends RuntimeException {
	private static final long serialVersionUID = 8028472103377808883L;

	public AlbumDataException() {
		super();
	}

	public AlbumDataException(String Message) {
		super(Message);
	}
}
