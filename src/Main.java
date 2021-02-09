import java.io.File;

import model.FileDrop;
import model.FileDrop.Listener;
import model.IFrameModel;
import view.IFrame;
import controller.ScaleThread;


/*
 * TODO: show filename while progressing
 * TODO: make it possible to add tasks while it is processing already
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

    @Override
    public void filesDropped(File[] files) {
        if (!this.frame.allInputValid())
            return;

        Thread t = new ScaleThread(this.frame, files);
        t.start();
    }

}
