package org.speedyfs.utils;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;

import javax.imageio.ImageIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.speedyfs.constants.AppConstants;
import org.speedyfs.enums.Thumbnail;
import org.speedyfs.exception.MediaException;
import org.speedyfs.model.ThumbFile;

public class ThumbnailUtility {
	private static Log log = LogFactory.getLog(ThumbnailUtility.class);

	public ThumbFile generateThumbnail(String path, String imgName) throws Exception {
		StringBuilder imagePath = new StringBuilder(path);
		try {
			BufferedImage originalImage = ImageIO.read(new File(path + File.separator + imgName));
			int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
			ThumbFile imageFile = new ThumbFile();
			for (Thumbnail thumb : Thumbnail.values()) {
				BufferedImage resizeImageJpg = resizeImageWithHint(originalImage, type, thumb.getvalue());
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ImageIO.write(resizeImageJpg,
						imgName.substring(imgName.lastIndexOf(AppConstants.DOT) + 1, imgName.length()), baos);
				switch (thumb) {
				case HIGH:
					imageFile.setLarge(baos.toByteArray());
					break;
				case MEDIUM:
					imageFile.setMedium(baos.toByteArray());
					break;
				case LOW:
					imageFile.setSmall(baos.toByteArray());
					break;
				}
			}
			return imageFile;
		} catch (Exception e) {
			log.error("Error while creating thumbnail Image for " + imagePath.append(File.separator).append(imgName));
			throw e;
		}
	}

	public String getVideoThumbnailName(String videoName, int size) {
		String extension = videoName.substring(videoName.lastIndexOf(AppConstants.DOT) + 1, videoName.length());
		String name = videoName.substring(0, videoName.lastIndexOf(AppConstants.DOT));
		return new StringBuilder(videoName.replace(".", AppConstants.UNDERSCORE)).append(AppConstants.UNDERSCORE)
				.append(size).append(AppConstants.DOT).append("png").toString();
	}

	public String getVideoThumbnailPath(String originalImgPath, Thumbnail thumbSize) {
		String albumPath = originalImgPath.substring(0, originalImgPath.lastIndexOf(File.separator));
		String imageName = originalImgPath.substring(originalImgPath.lastIndexOf(File.separator) + 1,
				originalImgPath.length());
		String thumbName = getVideoThumbnailName(imageName, thumbSize.getvalue());
		String thumbPath = new StringBuilder(albumPath).append(File.separator).append(AppConstants.THUMBNAIL_DIR_NAME)
				.append(File.separator).append(thumbName).toString();
		return thumbPath;
	}

	public String getThumbnailPath(String originalImgPath, Thumbnail thumbSize) {
		String albumPath = originalImgPath.substring(0, originalImgPath.lastIndexOf(File.separator));
		String imageName = originalImgPath.substring(originalImgPath.lastIndexOf(File.separator) + 1,
				originalImgPath.length());
		String thumbName = getThumbnailName(imageName, thumbSize.getvalue());
		String thumbPath = new StringBuilder(albumPath).append(File.separator).append(AppConstants.THUMBNAIL_DIR_NAME)
				.append(File.separator).append(thumbName).toString();
		return thumbPath;
	}

	public String getThumbnailName(String imgName, int size) {
		if (imgName.lastIndexOf(AppConstants.DOT) == -1) {
			log.error("File extention is missing for file: " + imgName);
			throw new MediaException("File extention is missing for file: " + imgName);
		}
		String extension = imgName.substring(imgName.lastIndexOf(AppConstants.DOT) + 1, imgName.length());
		String imageName = imgName.substring(0, imgName.lastIndexOf(AppConstants.DOT));
		return new StringBuilder(imageName).append(AppConstants.UNDERSCORE).append(size).append(AppConstants.DOT)
				.append(extension).toString();
	}

	public BufferedImage resizeImageWithHint(BufferedImage originalImage, int type, int size) {
		double origWidth = originalImage.getWidth();
		double origHeight = originalImage.getHeight();
		int newWidth = size;
		int newHeight = size;
		if (origWidth > origHeight) {
			newHeight = (int) ((origHeight / origWidth) * size);
		} else if (origHeight > origWidth) {
			newWidth = (int) ((origWidth / origHeight) * size);
		}
		BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
		g.setComposite(AlphaComposite.Src);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
		g.dispose();
		return resizedImage;
	}

	public void createThumbDirectories(String path) {
		File thumbDir = new File(path + File.separator + AppConstants.THUMBNAIL_DIR_NAME);
		if (!thumbDir.exists()) {
			thumbDir.mkdir();
		}
	}

	public void deleteThumbnails(String originalImgPath, boolean isVideo) {
		String albumPath = originalImgPath.substring(0, originalImgPath.lastIndexOf(File.separator));
		String imageName = originalImgPath.substring(originalImgPath.lastIndexOf(File.separator) + 1,
				originalImgPath.length());
		for (Thumbnail thumbSize : Thumbnail.values()) {
			String thumbName = (isVideo ? getVideoThumbnailName(imageName, thumbSize.getvalue())
					: getThumbnailName(imageName, thumbSize.getvalue()));
			String thumbPath = new StringBuilder(albumPath).append(File.separator)
					.append(AppConstants.THUMBNAIL_DIR_NAME).append(File.separator).append(thumbName).toString();
			new File(thumbPath).delete();
		}
	}

	public ThumbFile generateThumbAsync(String path, String imgName) {
		ThumbFile imageFile = null;
		try {
			imageFile = generateThumbnail(path, imgName);
		} catch (Exception e) {
			log.error("error while generating thumbnails thumb.getvalue() * thumb.getvalue() for " + path
					+ File.separator + imgName, e);
		}
		return imageFile;
	}

	public void generateVideoThumbAsync(String albumPath, String VideoName) {
		try {
			generateVideoThumnailByCmd(albumPath, VideoName);
		} catch (Exception e) {
			log.error("error while generating thumbnails for " + albumPath + File.separator + VideoName);
		}
	}

	/**
	 * Method to generate m3u8 and ts files required for hls streaming.
	 * 
	 * @param inputFilePath
	 * @param outputFilesPath
	 */
	public void generateHlsFiles(String photoId, String filePath, String albumPath) {
		log.info("ffmpeg command call to generate hls m3u8 files...........");
		String hlsDirectoryPath = albumPath + File.separator + photoId;
		File hlsDirectory = new File(hlsDirectoryPath);
		String m3u8FilePath = hlsDirectoryPath + File.separator + AppConstants.HLS_FILE_NAME;
		if (!hlsDirectory.exists()) {
			hlsDirectory.mkdir();
		}
		String inputFilePath = AppConstants.DOUBLE_QUOTE + filePath + AppConstants.DOUBLE_QUOTE;
		String outputFilesPath = AppConstants.DOUBLE_QUOTE + m3u8FilePath + AppConstants.DOUBLE_QUOTE;
		String command = "ffmpeg -i " + inputFilePath
				+ " -profile:v baseline -level 3.0 -s 640x360 -start_number 0 -hls_time 10 -hls_list_size 0 -f hls -strict experimental "
				+ outputFilesPath;
		log.info("Command executed to generate hls files::" + command);
		try {
			System.out.println("Launching command: " + command);
			ProcessBuilder pb = null;
			if (System.getProperty("os.name").contains("Windows")) {
				pb = new ProcessBuilder("cmd", "/c", command);
			} else if (System.getProperty("os.name").contains("Linux")) {
				pb = new ProcessBuilder("/bin/sh", "-c", command);
			} else {
				System.out.println(
						"Please Verify commend execution for this operating system:" + System.getProperty("os.name"));
				pb = new ProcessBuilder("/bin/sh", "-c", command);
			}
			Process proc = pb.start();

			PipeStream out = new PipeStream(proc.getInputStream(), System.out);
			PipeStream err = new PipeStream(proc.getErrorStream(), System.err);
			out.start();
			err.start();
			proc.waitFor();
			System.out.println("Exit value is: " + proc.exitValue());

		} catch (Exception e) {
			log.info("Error occured in generating hsl m3u8 ts files.", e);
		}
	}

	/**
	 * ffmpeg command to generate thumbnail
	 * https://superuser.com/questions/538112/meaningful-thumbnails-for-a-video-using-ffmpeg
	 * 
	 * @param inputFile
	 * @param thumbFile
	 * @return
	 */
	public int generateVideoThumnailByCmd(String path, String videoName) {
		StringBuilder videoPath = new StringBuilder(path);
		String inputFile = AppConstants.DOUBLE_QUOTE + path + File.separator + videoName + AppConstants.DOUBLE_QUOTE;
		String thumbFile = AppConstants.DOUBLE_QUOTE
				+ videoPath.append(File.separator).append(AppConstants.THUMBNAIL_DIR_NAME).append(File.separator)
						.append(videoName.replace(".", AppConstants.UNDERSCORE)).append(AppConstants.DOT).append("png")
						.toString()
				+ AppConstants.DOUBLE_QUOTE;
		String command = "ffmpeg -i " + inputFile + " -vf  \"thumbnail,scale=640:360\" -frames:v 1 " + thumbFile;
		int returnValue = -1;
		try {
			System.out.println("Launching command: " + command);
			ProcessBuilder pb = null;
			if (System.getProperty("os.name").contains("Windows")) {
				pb = new ProcessBuilder("cmd", "/c", command);
			} else if (System.getProperty("os.name").contains("Linux")) {
				pb = new ProcessBuilder("/bin/sh", "-c", command);
			} else {
				System.out.println(
						"Please Verify commend execution for this operating system:" + System.getProperty("os.name"));
				pb = new ProcessBuilder("/bin/sh", "-c", command);
			}
			Process proc = pb.start();

			PipeStream out = new PipeStream(proc.getInputStream(), System.out);
			PipeStream err = new PipeStream(proc.getErrorStream(), System.err);
			out.start();
			err.start();
			proc.waitFor();
			System.out.println("Exit value is: " + proc.exitValue());
		} catch (Exception e) {
			log.info("Error occured in Thumnail generation command.", e);
		}
		log.info("Thumnail generation command process terminate status code----" + returnValue);
		return returnValue;
	}
}
