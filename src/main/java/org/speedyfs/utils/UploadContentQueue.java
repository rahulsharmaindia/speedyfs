/**
 * 
 */
package org.speedyfs.utils;

import java.util.LinkedList;

import org.speedyfs.model.Content;

/**
 * @author himan
 *
 */
public class UploadContentQueue {

	private LinkedList<Content> contentQueue = null;
	private boolean readytoRun = false;

	private UploadContentQueue() {
		this.contentQueue = new LinkedList<Content>();
	}

	private static class SingletonHelper {
		private static final UploadContentQueue INSTANCE = new UploadContentQueue();
	}

	public static UploadContentQueue getInstance() {
		return SingletonHelper.INSTANCE;
	}

	public boolean uploadContent(Content content) throws Exception {
		boolean flag = false;
		if (contentQueue != null) {
			contentQueue.push(content);
			flag = true;
			if (contentQueue.size() >= 5) {
				setReadytoRun(true);
			}
		} else {
			throw new Exception("Invalid case with no element in contentList");
		}
		return flag;
	}

	public LinkedList<Content> popContentQueue() throws Exception {
		LinkedList<Content> contentList = null;
		if (contentQueue != null && contentQueue.size() >= 5) {
			contentList = this.contentQueue;
			this.contentQueue = new LinkedList<Content>();
			setReadytoRun(false);
		} else {
			throw new Exception("Invalid case with no element in contentQueue");
		}
		return contentList;
	}

	/**
	 * @return the readytoRun
	 */
	public boolean isReadytoRun() {
		return readytoRun;
	}

	/**
	 * @param readytoRun
	 *            the readytoRun to set
	 */
	protected void setReadytoRun(boolean readytoRun) {
		this.readytoRun = readytoRun;
	}
}
