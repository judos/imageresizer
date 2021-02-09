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

import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
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
import ch.judos.imageresizer.helper.KeyPressedAdapter;
import ch.judos.imageresizer.helper.KeyReleasedAdapter;
import ch.judos.imageresizer.model.AspectRatio;
import ch.judos.imageresizer.model.IFrameModel;
import ch.judos.imageresizer.model.SaveAction;

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
	public JComboBox<String> sizeComboBox;

	public IFrame(boolean visible) {
		setTitle("ImageResizer, by judos 2021©");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.imgSize = new Dimension(0, 0);
		this.imgAspectRatio = null;
		this.saveAction = null;

		initContent();
		this.sizeBorder = initPanel("Resize?", 0, 0);
		this.sizePanel = this.content;
		initSizeOfImages();

		this.aspectBorder = initPanel("Aspect ratio:", 0, 1);
		this.aspectPanel = this.content;
		this.aspectPanel.setVisible(false);
		initAspectRatio();

		this.targetBorder = initPanel("Choose target:", 0, 2);
		this.targetPanel = this.content;
		initTargetOfImages();

		this.progressBorder = initPanel("Progress:", 0, 3);
		this.progressBorder.setColor(Color.LIGHT_GRAY);
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
		b.setSelected(true);
		b = addButton(g, new JRadioButton("fit onto (selected size is minimum size)"), 0, 1);
		b.setActionCommand("fit_onto");
		b = addButton(g, new JRadioButton("force size (image might be distorted)"), 0, 2);
		b.setActionCommand("force");
		setAspectRatio(AspectRatio.FIT_INTO);
	}

	private void initProgress() {
		addComponent(this.progressPanel, new JLabel("Drag files here to convert them."), 0,
			0);
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
		final ButtonGroup btnGroup = new ButtonGroup();
		final ActionListener sizeAL = new SizeActionListener(this);
		this.listener = sizeAL;
		addButton(btnGroup, new JRadioButton("No"), 0, 0);

		JPanel dropdownPanel = new JPanel();
		dropdownPanel.setLayout(new GridBagLayout());
		JRadioButton chooseBtn =
			addButton(btnGroup, dropdownPanel, new JRadioButton("Choose"), 0, 0);

		this.sizeComboBox = new JComboBox<String>();
		sizeComboBox.addItem("1920 x 1080");
		sizeComboBox.addItem("1280 x 720");
		sizeComboBox.addItem("800 x 600");
		sizeComboBox.addActionListener((action) -> {
			btnGroup.setSelected(chooseBtn.getModel(), true);
			sizeAL.actionPerformed(
				new ActionEvent(sizeComboBox, 0, (String) sizeComboBox.getSelectedItem()));
		});
		addComponent(dropdownPanel, sizeComboBox, 1, 0);
		addComponent(this.content, dropdownPanel, 0, 1);

		JPanel customPanel = new JPanel();
		customPanel.setLayout(new GridBagLayout());
		final JRadioButton c =
			addButton(btnGroup, customPanel, new JRadioButton("Custom:"), 0, 0);
		this.customX = (JTextField) addComponent(customPanel, new JTextField(), 1, 0);
		this.customX.setPreferredSize(getTextFieldSize(5));
		this.customX.addKeyListener((KeyPressedAdapter) (e) -> {
			sizeAL.actionPerformed(new ActionEvent(customX, 0, "Custom:"));
			btnGroup.setSelected(c.getModel(), true);
		});
		addComponent(customPanel, new JLabel("x"), 2, 0);
		this.customY = (JTextField) addComponent(customPanel, new JTextField(), 3, 0);
		this.customY.setPreferredSize(getTextFieldSize(5));
		this.customY.addKeyListener((KeyReleasedAdapter) (e) -> {
			sizeAL.actionPerformed(new ActionEvent(customY, 0, "Custom:"));
			btnGroup.setSelected(c.getModel(), true);
		});

		addComponent(this.content, customPanel, 0, 2);
	}

	private JRadioButton addButton(ButtonGroup g, JPanel container,
		JRadioButton jRadioButton, int x, int y) {
		addComponent(container, jRadioButton, x, y);
		g.add(jRadioButton);
		if (this.listener != null)
			jRadioButton.addActionListener(this.listener);
		return jRadioButton;
	}

	private JRadioButton addButton(ButtonGroup g, JRadioButton jRadioButton, int x, int y) {
		addButton(g, this.content, jRadioButton, x, y);
		return jRadioButton;
	}

	private Component addComponent(JPanel container, Component comp, int x, int y) {
		return addComponent(container, comp, x, y, 1, 1);
	}

	private Dimension getTextFieldSize(int characters) {
		JComponent comp = new JPanel();
		FontMetrics m = comp.getFontMetrics(getFont());
		int h = m.getHeight();
		int w = m.stringWidth("w");
		return new Dimension(w * characters, h);
	}

	private Component addComponent(JPanel container, Component comp, int x, int y,
		int width, int height) {
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
		c.weighty = 1;
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

	public void setResizeDimension(Dimension dimension) {
		this.imgSize = dimension;
		this.imgSizeValid = true;
		this.sizeBorder.setColor(Color.green);
		this.sizePanel.repaint();
		this.aspectPanel.setVisible(this.imgSize != null);
		this.pack();
	}

	public void setTargetAction(SaveAction s) {
		this.targetBorder.setColor(Color.green);
		this.saveAction = s;
		this.targetPanel.repaint();
	}

	public SaveAction getSaveAction() {
		return this.saveAction;
	}

	public Dimension getTargetDimension() {
		return this.imgSize;
	}

	public void setAspectRatio(AspectRatio r) {
		this.imgAspectRatio = r;
		this.aspectBorder.setColor(Color.green);
		this.aspectPanel.repaint();
	}

	public AspectRatio getAspectRatio() {
		return this.imgAspectRatio;
	}

	public void setProgress(float progress) {
		this.progressBorder.setColor(Color.orange);
		this.progressPanel.removeAll();

		JProgressBar p = new JProgressBar(0, 100);
		p.setBackground(new Color(200, 180, 180));
		p.setForeground(new Color(150, 250, 150));
		p.setValue((int) (progress * 100));
		c.fill = GridBagConstraints.HORIZONTAL;
		addComponent(this.progressPanel, p, 0, 0);
		int per = (int) (progress * 100);
		addComponent(this.progressPanel, new JLabel(per + " % finished"), 0, 1);
		this.progressPanel.validate();
		pack();
	}

	public boolean allInputValid() {
		if (!this.imgSizeValid) {
			System.out.println("Size invalid");
			return false;
		}
		if (this.imgAspectRatio == null) {
			System.out.println("missing aspect ratio");
			return false;
		}
		if (this.saveAction == null) {
			System.out.println("Missing save action");
			return false;
		}
		return true;
	}

	public void setProgressFinished() {
		this.progressBorder.setColor(Color.green);
		this.progressPanel.removeAll();
		initProgress();
		this.progressPanel.validate();
		pack();
	}
}
