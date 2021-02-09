package ch.judos.imageresizer.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ch.judos.imageresizer.view.IFrame;
import ch.judos.imageresizermodel.SaveAction;

public class TargetActionListener implements ActionListener {

	private IFrame frame;

	public TargetActionListener(IFrame iFrame) {
		this.frame = iFrame;
	}

	public void actionPerformed(ActionEvent arg0) {
		String cmd = arg0.getActionCommand();
		this.frame.setTargetAction(SaveAction.fromString(cmd));
	}

}
