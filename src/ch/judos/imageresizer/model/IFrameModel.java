package ch.judos.imageresizer.model;

import java.awt.Dimension;

public interface IFrameModel {

	public SaveAction getSaveAction();

	public Dimension getTargetDimension();

	public AspectRatio getAspectRatio();

	public void setProgress(float progress);

	public boolean allInputValid();

	public void setProgressFinished();

}
