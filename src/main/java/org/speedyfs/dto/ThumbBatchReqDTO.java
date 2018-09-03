package org.speedyfs.dto;

import java.util.List;

public class ThumbBatchReqDTO {
	private List<Long> imageIds;

	public List<Long> getImageIds() {
		return imageIds;
	}

	public void setImageIds(List<Long> imageIds) {
		this.imageIds = imageIds;
	}
}
