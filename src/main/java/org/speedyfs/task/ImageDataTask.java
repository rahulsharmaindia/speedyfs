package org.speedyfs.task;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import org.speedyfs.service.MediaDataTaskService;

/**
 * <p>
 * Scheduled Task Bean for scheduling jobs using cron expressions.
 * @author rahul.sharma3
 *
 */
@Component
public class ImageDataTask {
	@Autowired
	MediaDataTaskService taskService;
	
	private Log log = LogFactory.getLog(this.getClass());

	/**
	 * <p>
	 * Scheduler to correct the creation date of contents in database, If not correct or null.
	 */
	@Scheduled(cron = "${imagedata.expression}")
	public void checkCreationDate() {
		log.info("Scheduler executing for Creation Date Cleanup");
		taskService.updateNullCreationDateData();
	}
}
