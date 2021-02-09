package ch.judos.imageresizer.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ch.judos.imageresizer.model.AspectRatio;
import ch.judos.imageresizer.view.IFrame;

public class AspectRatioActionListener implements ActionListener {

	private IFrame frame;

	public AspectRatioActionListener(IFrame iFrame) {
		this.frame = iFrame;
	}

	public void actionPerformed(ActionEvent arg0) {
		String cmd = arg0.getActionCommand();
		AspectRatio r = AspectRatio.fromString(cmd);
		this.frame.setAspectRatio(r);
	}

}
