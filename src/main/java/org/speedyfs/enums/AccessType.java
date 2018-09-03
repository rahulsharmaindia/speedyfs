package org.speedyfs.enums;

public enum AccessType {
	PUBLIC(1), SHARED(2), PRIVATE(0);
	int value;

	private AccessType(int quality) {
		this.value = quality;
	}

	public int getvalue() {
		return value;
	}
}
