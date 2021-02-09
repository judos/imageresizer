package ch.judos.imageresizer.view;

import java.awt.Color;

import javax.swing.border.LineBorder;

public class RecolorableLineBorder extends LineBorder {

	public RecolorableLineBorder(Color color, int thickness,
		boolean roundedCorners) {
		super(color, thickness, roundedCorners);
	}

	public RecolorableLineBorder(Color color, int thickness) {
		this(color, thickness, false);
	}

	public RecolorableLineBorder(Color color) {
		this(color, 1, false);
	}

	private static final long serialVersionUID = -3322204003758724033L;

	public void setColor(Color color) {
		this.lineColor = color;
	}
}
