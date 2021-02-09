package ch.judos.imageresizer;

import java.io.File;

import ch.judos.imageresizer.controller.ScaleThread;
import ch.judos.imageresizer.view.IFrame;
import ch.judos.imageresizermodel.FileDrop;
import ch.judos.imageresizermodel.FileDrop.Listener;
import ch.judos.imageresizermodel.IFrameModel;

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
		if (!this.frame.allInputValid())
			return;

		Thread t = new ScaleThread(this.frame, files);
		t.start();
	}

}
