package ch.judos.imageresizer.helper;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

@FunctionalInterface
public interface KeyReleasedAdapter extends KeyListener {

	@Override
	default void keyPressed(KeyEvent e) {}

	@Override
	default void keyTyped(KeyEvent e) {}

}
