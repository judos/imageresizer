package ch.judos.imageresizer.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ch.judos.imageresizer.view.IFrame;
import ch.judos.imageresizermodel.AspectRatio;

public class AspectRatioActionListener implements ActionListener {

	private IFrame frame;

	public AspectRatioActionListener(IFrame iFrame) {
		this.frame = iFrame;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		String cmd = arg0.getActionCommand();
		AspectRatio r = AspectRatio.fromString(cmd);
		this.frame.setAspectRatio(r);
	}

}
