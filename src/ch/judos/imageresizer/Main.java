package ch.judos.imageresizer;

import java.io.File;

import ch.judos.imageresizer.controller.ScaleThread;
import ch.judos.imageresizer.model.FileDrop;
import ch.judos.imageresizer.model.FileDrop.Listener;
import ch.judos.imageresizer.model.IFrameModel;
import ch.judos.imageresizer.view.IFrame;

/**
 * TODO: show filename while progressing<br>
 * TODO: make it possible to add tasks while it is processing already<br>
 * TODO: an animation while processing, so the user knows its doing something
 */
public class Main implements Listener {

	public static void main(String[] args) {
		new Main();
	}

	private IFrameModel frame;

	public Main() {
		IFrame f = new IFrame(true);
		new FileDrop(f, this);
		this.frame = f;
	}

	public void filesDropped(File[] files) {
		System.out.println("start conversion");
		if (!this.frame.allInputValid())
			return;

		Thread t = new ScaleThread(this.frame, files);
		t.start();
	}

}
