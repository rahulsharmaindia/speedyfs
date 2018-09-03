package org.speedyfs.enums;

public enum Thumbnail {
	HIGH(500), MEDIUM(250), LOW(50);
	int value;

	private Thumbnail(int quality) {
		this.value = quality;
	}

	public int getvalue() {
		return value;
	}
}
