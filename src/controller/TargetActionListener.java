package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import model.SaveAction;
import view.IFrame;

public class TargetActionListener implements ActionListener {

	private IFrame frame;

	public TargetActionListener(IFrame iFrame) {
		this.frame = iFrame;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		String cmd = arg0.getActionCommand();
		this.frame.setTargetAction(SaveAction.fromString(cmd));
	}

}
