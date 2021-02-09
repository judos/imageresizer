package ch.judos.imageresizer.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import ch.judos.imageresizer.controller.AspectRatioActionListener;
import ch.judos.imageresizer.controller.SizeActionListener;
import ch.judos.imageresizer.controller.TargetActionListener;
import ch.judos.imageresizermodel.AspectRatio;
import ch.judos.imageresizermodel.IFrameModel;
import ch.judos.imageresizermodel.SaveAction;

public class IFrame extends JFrame implements IFrameModel {
	private static final long serialVersionUID = 3819774890772267562L;
	private JPanel content;
	private GridBagConstraints c;
	private JTextField customX;
	private JTextField customY;
	private boolean imgSizeValid;
	private Dimension imgSize;
	private ActionListener listener;
	private RecolorableLineBorder sizeBorder;
	private RecolorableLineBorder targetBorder;
	private JPanel sizePanel;
	private JPanel targetPanel;
	private SaveAction saveAction;
	private RecolorableLineBorder progressBorder;
	private JPanel progressPanel;
	private RecolorableLineBorder aspectBorder;
	private JPanel aspectPanel;
	private AspectRatio imgAspectRatio;

	public IFrame(boolean visible) {
		setTitle("ImageResizer, by judos 2021©");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.imgSize = new Dimension(0, 0);
		this.imgAspectRatio = null;
		this.saveAction = null;

		initContent();
		this.sizeBorder = initPanel("Choose size:", 0, 0);
		this.sizePanel = this.content;
		initSizeOfImages();

		this.aspectBorder = initPanel("Aspect ratio:", 0, 1);
		this.aspectPanel = this.content;
		initAspectRatio();

		this.targetBorder = initPanel("Choose target:", 0, 2);
		this.targetPanel = this.content;
		initTargetOfImages();

		this.progressBorder = initPanel("Progress:", 0, 3);
		this.progressBorder.setColor(Color.green);
		this.progressPanel = this.content;
		initProgress();

		setResizable(false);
		pack();
		setLocationRelativeTo(null);
		setVisible(visible);
	}

	private void initAspectRatio() {
		ButtonGroup g = new ButtonGroup();
		this.listener = new AspectRatioActionListener(this);
		JRadioButton b;
		b = addButton(g, new JRadioButton("fit into (selected size is maximal size)"), 0, 0);
		b.setActionCommand("fit_into");
		b = addButton(g, new JRadioButton("fit onto (selected size is minimum size)"), 0, 1);
		b.setActionCommand("fit_onto");
		b = addButton(g, new JRadioButton("force size (image might be distorted)"), 0, 2);
		b.setActionCommand("force");
	}

	private void initProgress() {
		addP(this.progressPanel, new JLabel("Drag files here to convert them."), 0, 0);
	}

	private void initTargetOfImages() {
		ButtonGroup g = new ButtonGroup();
		this.listener = new TargetActionListener(this);
		JRadioButton b;

		b = addButton(g, new JRadioButton("Overwrite file"), 0, 1);
		b.setActionCommand("overwrite");
		b = addButton(g, new JRadioButton("Same folder, append \"width x height\""), 0, 2);
		b.setActionCommand("append_size");

		b = addButton(g, new JRadioButton("Choose folder for all picture"), 0, 3);
		b.setActionCommand("choose_folder");
		b = addButton(g, new JRadioButton("Choose path & filename for every picture"), 0, 4);
		b.setActionCommand("choose_all");
	}

	private void initSizeOfImages() {
		final ButtonGroup g = new ButtonGroup();
		final ActionListener sizeAL = new SizeActionListener(this);
		this.listener = new SizeActionListener(this);
		addButton(g, new JRadioButton("320 x 240"), 0, 1);
		addButton(g, new JRadioButton("640 x 480"), 0, 2);
		addButton(g, new JRadioButton("800 x 600"), 0, 3);

		addButton(g, new JRadioButton("1024 x 768"), 1, 1);
		addButton(g, new JRadioButton("1280 x 1024"), 1, 2);
		addButton(g, new JRadioButton("1600 x 1200"), 1, 3);

		JPanel custom = new JPanel();
		custom.setLayout(new GridBagLayout());
		final JRadioButton c = addButton(g, custom, new JRadioButton("Custom:"), 0, 0);
		this.customX = (JTextField) addP(custom, new JTextField(), 1, 0);
		this.customX.setPreferredSize(getTextFieldSize(5));
		this.customX.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				sizeAL.actionPerformed(new ActionEvent(customX, 0, "Custom:"));
				g.setSelected(c.getModel(), true);
			}
		});
		addP(custom, new JLabel("x"), 2, 0);
		this.customY = (JTextField) addP(custom, new JTextField(), 3, 0);
		this.customY.setPreferredSize(getTextFieldSize(5));
		this.customY.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				sizeAL.actionPerformed(new ActionEvent(customY, 0, "Custom:"));
				g.setSelected(c.getModel(), true);
			}
		});

		addP(custom, 0, 4, 2, 1);
	}

	private JRadioButton addButton(ButtonGroup g, JPanel container, JRadioButton jRadioButton, int x, int y) {
		addP(container, jRadioButton, x, y);
		g.add(jRadioButton);
		if (this.listener != null)
			jRadioButton.addActionListener(this.listener);
		return jRadioButton;
	}

	private JRadioButton addButton(ButtonGroup g, JRadioButton jRadioButton, int x, int y) {
		addButton(g, this.content, jRadioButton, x, y);
		return jRadioButton;
	}

	@SuppressWarnings("unused")
	private Component addP(Component comp, int x, int y) {
		addP(comp, x, y, 1, 1);
		return comp;
	}

	private Component addP(Component comp, int x, int y, int width, int height) {
		return addP(this.content, comp, x, y, width, height);
	}

	private Component addP(JPanel container, Component comp, int x, int y) {
		return addP(container, comp, x, y, 1, 1);
	}

	private Dimension getTextFieldSize(int characters) {
		JComponent comp = new JPanel();
		FontMetrics m = comp.getFontMetrics(getFont());
		int h = m.getHeight();
		int w = m.stringWidth("w");
		return new Dimension(w * characters, h);
	}

	private Component addP(JPanel container, Component comp, int x, int y, int width, int height) {
		comp.setFont(getFont());
		c.gridx = x;
		c.gridy = y;
		c.gridwidth = width;
		c.gridheight = height;
		container.add(comp, c);
		return comp;
	}

	private void initContent() {
		setFont(new Font("Arial", 0, 18));
		this.c = new GridBagConstraints();
		c.insets = new Insets(2, 5, 2, 5);
		c.gridy = 0;
		c.weightx = 1;
		c.anchor = GridBagConstraints.WEST;
		setLayout(new GridBagLayout());
	}

	private RecolorableLineBorder initPanel(String title, int x, int y) {
		this.content = new JPanel();
		c.gridx = x;
		c.gridy = y;
		c.fill = GridBagConstraints.HORIZONTAL;
		add(this.content, c);
		c.fill = GridBagConstraints.NONE;
		this.content.setLayout(new GridBagLayout());
		JLabel titleL = new JLabel(" " + title + " ");
		titleL.setForeground(new Color(0, 0, 128));
		titleL.setOpaque(true);
		titleL.setFont(getFont());
		RecolorableLineBorder lineBorder = new RecolorableLineBorder(Color.red, 2);
		this.content.setBorder(new ComponentTitledBorder(titleL, content, lineBorder));
		return lineBorder;
	}

	public int getCustomSizeX() {
		return Integer.valueOf(this.customX.getText());
	}

	public int getCustomSizeY() {
		return Integer.valueOf(this.customY.getText());
	}

	public void sizeUnset() {
		this.imgSizeValid = false;
		this.sizeBorder.setColor(Color.red);
		this.sizePanel.repaint();
	}

	public void set(Dimension dimension) {
		this.imgSize = dimension;
		this.imgSizeValid = true;
		this.sizeBorder.setColor(Color.green);
		this.sizePanel.repaint();
	}

	public void setTargetAction(SaveAction s) {
		this.targetBorder.setColor(Color.green);
		this.saveAction = s;
		this.targetPanel.repaint();
	}

	@Override
	public SaveAction getSaveAction() {
		return this.saveAction;
	}

	@Override
	public Dimension getTargetDimension() {
		return this.imgSize;
	}

	public void setAspectRatio(AspectRatio r) {
		this.imgAspectRatio = r;
		this.aspectBorder.setColor(Color.green);
		this.aspectPanel.repaint();
	}

	@Override
	public AspectRatio getAspectRatio() {
		return this.imgAspectRatio;
	}

	@Override
	public void setProgress(float progress) {
		this.progressBorder.setColor(Color.orange);
		this.progressPanel.removeAll();

		JProgressBar p = new JProgressBar(0, 100);
		p.setBackground(new Color(200, 180, 180));
		p.setForeground(new Color(150, 250, 150));
		p.setValue((int) (progress * 100));
		c.fill = GridBagConstraints.HORIZONTAL;
		addP(this.progressPanel, p, 0, 0);
		int per = (int) (progress * 100);
		addP(this.progressPanel, new JLabel(per + " % finished"), 0, 1);
		this.progressPanel.validate();
		pack();
	}

	@Override
	public boolean allInputValid() {
		if (!this.imgSizeValid)
			return false;
		if (this.imgAspectRatio == null)
			return false;
		if (this.saveAction == null)
			return false;
		return true;
	}

	@Override
	public void setProgressFinished() {
		this.progressBorder.setColor(Color.green);
		this.progressPanel.removeAll();
		initProgress();
		this.progressPanel.validate();
		pack();
	}
}
