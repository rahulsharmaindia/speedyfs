package org.speedyfs.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
@Deprecated
public enum MimeType {
	MSWORD("application/msword", 32), PDF("application/pdf", 16), IMAGE("image", 1), VIDEO("video",
			4), UNKNOWN("unknown", -1);
	String value;
	int flag;

	private MimeType(String type, int flag) {
		this.value = type;
		this.flag = flag;
	}

	public String getValue() {
		return value;
	}

	public int getFlag() {
		return flag;
	}

	private static final Map<String, MimeType> map;
	static {
		map = Arrays.stream(values()).collect(Collectors.toMap(e -> e.value, e -> e));
	}

	public static MimeType getKey(String value) {
		return Optional.ofNullable(map.get(value)).orElse(UNKNOWN);
	}
}
