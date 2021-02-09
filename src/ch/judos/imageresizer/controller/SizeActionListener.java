package ch.judos.imageresizer.controller;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.judos.imageresizer.view.IFrame;

public class SizeActionListener implements ActionListener {

	private IFrame frame;

	public SizeActionListener(IFrame iFrame) {
		this.frame = iFrame;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		String button = arg0.getActionCommand();
		Pattern p = Pattern.compile("(\\d+) x (\\d+)");
		Matcher m = p.matcher(button);
		if (m.matches()) {
			this.frame.set(new Dimension(Integer.valueOf(m.group(1)), Integer
				.valueOf(m.group(2))));
		} else if (button.equals("Custom:")) {
			try {
				this.frame.set(new Dimension(this.frame.getCustomSizeX(),
					this.frame.getCustomSizeY()));
				if (this.frame.getCustomSizeX() == 0
					|| this.frame.getCustomSizeY() == 0)
					this.frame.sizeUnset();
			} catch (Exception e) {
				this.frame.sizeUnset();
			}

		}
	}
}
