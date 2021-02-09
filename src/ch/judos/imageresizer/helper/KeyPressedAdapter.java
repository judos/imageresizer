package ch.judos.imageresizer.helper;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

@FunctionalInterface
public interface KeyPressedAdapter extends KeyListener {

	@Override
	default void keyReleased(KeyEvent e) {}

	@Override
	default void keyTyped(KeyEvent e) {}

}
