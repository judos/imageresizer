package ch.judos.imageresizer.view;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

/**
 * Changed by Julian Schelker, 2010 <br>
 * added the possibility to aligne the component<br>
 * 
 * MySwing: Advanced Swing Utilites 3 * Copyright (C) 2005 Santhosh Kumar T
 * 
 * This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; either version
 * 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * @since ??.??.2010
 * @author Santhosh Kumar T, Julian Schelker
 * @version 1.01 / 27.02.2013
 */

public class ComponentTitledBorder
	implements Border, MouseListener, MouseMotionListener, SwingConstants {

	/**
	 * an example to show you what this class does
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		final JPanel proxyPanel = new JPanel();
		proxyPanel.add(new JLabel("Proxy Host: "));
		proxyPanel.add(new JTextField("proxy.xyz.com"));
		proxyPanel.add(new JLabel("  Proxy Port"));
		proxyPanel.add(new JTextField("8080"));
		final JCheckBox checkBox = new JCheckBox("Use Proxy", true);
		checkBox.setFocusPainted(false);
		ComponentTitledBorder componentBorder =
			new ComponentTitledBorder(checkBox, proxyPanel, BorderFactory.createEtchedBorder());
		checkBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean enable = checkBox.isSelected();
				Component comp[] = proxyPanel.getComponents();
				for (int i = 0; i < comp.length; i++) {
					comp[i].setEnabled(enable);
				}
			}
		});
		proxyPanel.setBorder(componentBorder);
		JFrame frame = new JFrame("ComponentTitledBorder - santhosh@in.fiorano.com");
		Container contents = frame.getContentPane();
		contents.setLayout(new FlowLayout());
		contents.add(proxyPanel);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	int offset = 5;

	alignement componentAlignement;

	enum alignement {
		LEFT, CENTER, RIGHT
	}

	private int left;
	private Component comp;
	private JComponent container;
	private Rectangle rect;
	private Border border;
	private boolean mouseEntered = false;

	/**
	 * standard alignement (left)
	 * 
	 * @param comp
	 * @param container
	 * @param border
	 */
	public ComponentTitledBorder(Component comp, JComponent container, Border border) {
		this(comp, container, border, alignement.LEFT);
	}

	/**
	 * @param comp
	 * @param container
	 * @param border
	 * @param compAlignement
	 */
	public ComponentTitledBorder(Component comp, JComponent container, Border border,
		alignement compAlignement) {
		this.comp = comp;
		this.componentAlignement = compAlignement;
		this.container = container;
		this.border = border;
		left = offset;
		container.addMouseMotionListener(this);
		container.addMouseListener(this);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.border.Border#isBorderOpaque()
	 */
	public boolean isBorderOpaque() {
		return true;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.border.Border#paintBorder(java.awt.Component, java.awt.Graphics, int, int,
	 *      int, int)
	 */
	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		Insets borderInsets = border.getBorderInsets(c);
		Insets insets = getBorderInsets(c);
		int temp = (insets.top - borderInsets.top) / 2;
		border.paintBorder(c, g, x, y + temp, width, height - temp);
		Dimension size = comp.getPreferredSize();
		switch (this.componentAlignement) {
			case LEFT:
				left = offset;
				break;
			case CENTER:
				left = width / 2 - size.width / 2;
				break;
			case RIGHT:
				left = width - size.width - offset;
		}
		rect = new Rectangle(left, 0, size.width, size.height);

		SwingUtilities.paintComponent(g, comp, (Container) c, rect);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.border.Border#getBorderInsets(java.awt.Component)
	 */
	public Insets getBorderInsets(Component c) {
		Dimension size = comp.getPreferredSize();
		Insets insets = border.getBorderInsets(c);
		insets.top = Math.max(insets.top, size.height);
		return insets;
	}

	private void dispatchEvent(MouseEvent me) {
		if (rect != null && rect.contains(me.getX(), me.getY())) {
			dispatchEvent(me, me.getID());
		}
		else
			dispatchEvent(me, MouseEvent.MOUSE_EXITED);
	}

	private void dispatchEvent(MouseEvent me, int id) {
		Point pt = me.getPoint();
		pt.translate(-rect.x, 0);

		comp.setSize(rect.width, rect.height);
		comp.dispatchEvent(new MouseEvent(comp, id, me.getWhen(), me.getModifiersEx(), pt.x,
			pt.y, me.getClickCount(), me.isPopupTrigger(), me.getButton()));
		if (!comp.isValid()) {
			container.repaint();
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent me) {
		dispatchEvent(me);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited(MouseEvent me) {
		if (mouseEntered) {
			mouseEntered = false;
			dispatchEvent(me, MouseEvent.MOUSE_EXITED);
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent me) {
		dispatchEvent(me);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent me) {
		dispatchEvent(me);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
	 */
	public void mouseDragged(MouseEvent e) {}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 */
	public void mouseMoved(MouseEvent me) {
		if (rect == null) {
			return;
		}

		if (mouseEntered == false && rect.contains(me.getX(), me.getY())) {
			mouseEntered = true;
			dispatchEvent(me, MouseEvent.MOUSE_ENTERED);
		}
		else if (mouseEntered == true) {
			if (rect.contains(me.getX(), me.getY()) == false) {
				mouseEntered = false;
				dispatchEvent(me, MouseEvent.MOUSE_EXITED);
			}
			else {
				dispatchEvent(me, MouseEvent.MOUSE_MOVED);
			}
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered(MouseEvent e) {}

}
