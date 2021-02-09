package ch.judos.imageresizer.controller;

import java.awt.Dimension;
import java.awt.Image;
import java.io.File;

import javax.imageio.ImageIO;

import ch.judos.imageresizermodel.AspectRatio;
import ch.judos.imageresizermodel.FileJS;
import ch.judos.imageresizermodel.IFrameModel;
import ch.judos.imageresizermodel.ImageJS;
import ch.judos.imageresizermodel.Notification;
import ch.judos.imageresizermodel.ProgressIterable;
import ch.judos.imageresizermodel.SaveAction;
import ch.judos.imageresizermodel.ProgressIterable.Progress;

public class ScaleThread extends Thread {

	private IFrameModel frame;
	private File storeAllDir;
	private File[] files;
	static File directoryStartLookAt = FileJS.getDesktopDir();

	public ScaleThread(IFrameModel frame, File[] files) {
		this.frame = frame;
		this.files = files;
	}

	@Override
	public void run() {
		SaveAction save = this.frame.getSaveAction();
		Dimension size = this.frame.getTargetDimension();
		AspectRatio ratio = this.frame.getAspectRatio();
		this.storeAllDir = null;
		String error = "";
		int errorCount = 0;

		ProgressIterable<File> list = new ProgressIterable<File>(files);
		for (Progress<File> p : list) {
			try {
				FileJS file = new FileJS(p.getObject());

				this.frame.setProgress(p.getProgress());

				Image i = ImageJS.loadBufferedImage(file);
				if (i == null)
					throw new Exception("Couldn't load image: " + file.getName());
				i = transformImage(i, ratio, size);

				File targetFile = getTargetFile(save, size, file);
				ImageIO.write(ImageJS.toBufferedImage(i), file.getEnding(), targetFile);
			} catch (Exception e) {
				error += e.getLocalizedMessage() + "\n\n";
				errorCount++;
			}
		}
		this.frame.setProgress(1);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e1) {}

		this.frame.setProgressFinished();
		if (errorCount > 0) {
			String e = "errors";
			if (errorCount == 1)
				e = "error";
			Notification.notifyErr(errorCount + " " + e + " occured", error);
		}

	}

	private File getTargetFile(SaveAction save, Dimension size, FileJS file) {
		if (save == SaveAction.OVERWRITE)
			return file;
		else if (save == SaveAction.APPEND_SIZE)
			return new File(file.getParent(), file.getNameWithoutEnding() + " "
				+ size.width + "x" + size.height + "." + file.getEnding());
		else if (save == SaveAction.CHOOSE_FOLDER) {
			if (this.storeAllDir == null) {
				directoryStartLookAt =
					FileJS.requestDir("Choose the directory to store all images:",
						directoryStartLookAt);
				this.storeAllDir = directoryStartLookAt;
			}
			return new File(directoryStartLookAt, file.getName());
		} else /* if (save==SaveAction.CHOOSE_ALL) */{
			File f =
				FileJS.saveFile(directoryStartLookAt,
					"Choose the place where to store \"" + file.getName() + "\":",
					new String[] { "jpg", "png" });
			directoryStartLookAt = f.getParentFile();
			return f;
		}
	}

	private Image transformImage(Image i, AspectRatio ratio, Dimension size) {
		if (ratio == AspectRatio.FIT_INTO)
			i = ImageJS.fitInto(i, size);
		else if (ratio == AspectRatio.FIT_ONTO)
			i = ImageJS.fitOnto(i, size);
		else
			i = i.getScaledInstance(size.width, size.height, Image.SCALE_SMOOTH);
		return i;
	}
}
