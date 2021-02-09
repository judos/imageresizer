package ch.judos.imageresizermodel;

import java.awt.Component;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

/**
 * simplifies many tasks like requesting the user to input a path to a directory
 * 
 * @since ??.??.2011
 * @author Julian Schelker
 * @version 1.1 / 02.06.2013
 * 
 */
public class FileJS extends File {
	public FileJS(String pathname) {
		super(pathname);
	}

	public FileJS(File f) {
		super(f.getAbsolutePath());
	}

	public String getEnding() {
		return splitName()[1];
	}

	public String getNameWithoutEnding() {
		return splitName()[0];
	}

	private String[] splitName() {
		String fileName = getName();
		Pattern pattern = Pattern.compile("(.+)\\.(\\w+)");
		Matcher m = pattern.matcher(fileName);
		if (m.matches()) {
			String name = m.group(1);
			String ending = m.group(2);
			return new String[] { name, ending };
		}
		throw new RuntimeException("Problem with splitting fileName: " + fileName);
	}

	private static final long serialVersionUID = 1983089077035243846L;

	/**
	 * @param f
	 * @return BufferedReader to read the file
	 * @throws FileNotFoundException
	 *             - if the file does not exist, is a directory rather than a
	 *             regular file, or for some other reason cannot be opened for
	 *             reading.
	 */
	public static BufferedReader readFile(File f) throws FileNotFoundException {
		return new BufferedReader(new FileReader(f));
	}

	/**
	 * @param name
	 * @return BufferedReader to read the file
	 * @throws FileNotFoundException
	 *             - if the file does not exist, is a directory rather than a
	 *             regular file, or for some other reason cannot be opened for
	 *             reading.
	 */
	public static BufferedReader readFile(String name) throws FileNotFoundException {
		return new BufferedReader(new FileReader(new File(name)));
	}

	/**
	 * @param f
	 * @return BufferedWriter to write into the file
	 * @throws IOException
	 *             - if the file exists but is a directory rather than a regular
	 *             file, does not exist but cannot be created, or cannot be
	 *             opened for any other reason
	 */
	public static BufferedWriter writeFile(File f) throws IOException {
		return new BufferedWriter(new FileWriter(f));
	}

	/**
	 * @param name
	 * @return BufferedWriter to write into the file
	 * @throws IOException
	 *             - if the file exists but is a directory rather than a regular
	 *             file, does not exist but cannot be created, or cannot be
	 *             opened for any other reason
	 */
	public static BufferedWriter writeFile(String name) throws IOException {
		return new BufferedWriter(new FileWriter(new File(name)));
	}

	/**
	 * @return directory information of the desktop
	 */
	public static File getDesktopDir() {
		FileSystemView filesys = FileSystemView.getFileSystemView();
		// File[] roots = filesys.getRoots();
		return filesys.getHomeDirectory();
	}

	/**
	 * @param description
	 *            some text to display to the user
	 * @param extensions
	 *            array of the extensions allowed
	 * @return the file the user entered - null if cancel was pressed
	 */
	public static File requestFile(String description, String extensions[]) {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter =
			new FileNameExtensionFilter(description, extensions);
		chooser.setFileFilter(filter);
		int returnVal = chooser.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			return chooser.getSelectedFile();
		} else
			return null;
	}

	/**
	 * @param title
	 *            displayed to the user
	 * @param startAt
	 *            initial directory to come up with
	 * @return the directory selected or null if operation is canceled
	 */
	public static File requestDir(String title, File startAt) {
		return requestDir(null, title, startAt);
	}

	/**
	 * @param title
	 *            displayed to the user
	 * @return the directory selected or null if operation is canceled
	 */
	public static File requestDir(String title) {
		return requestDir(null, title, new File("."));
	}

	/**
	 * @param parent
	 *            component of the popup
	 * @param title
	 *            displayed to the user
	 * @return the directory selected or null if operation is canceled
	 */
	public static File requestDir(Component parent, String title) {
		return requestDir(parent, title, new File("."));
	}

	/**
	 * @param parent
	 *            component of the popup
	 * @param title
	 *            displayed to the user
	 * @param startAt
	 *            initial directory to come up with
	 * @return the directory selected or null if operation is canceled
	 */
	public static File requestDir(Component parent, String title, File startAt) {
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(startAt);
		chooser.setDialogTitle(title);
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);
		if (chooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
			return chooser.getSelectedFile();
		} else {
			return null;
		}
	}

	/**
	 * @return the file the user entered - null if cancel was pressed
	 */
	public static File requestFile() {
		JFileChooser chooser = new JFileChooser();
		int returnVal = chooser.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			return chooser.getSelectedFile();
		} else
			return null;
	}

	/**
	 * @param startPath
	 *            initial directory to come up with
	 * @param description
	 *            displayed to the user
	 * @param extensions
	 *            which are allowed
	 * @return the selected file or null is the operation is canceled
	 */
	public static File
		requestFile(File startPath, String description, String[] extensions) {
		return requestFile(startPath.getPath(), description, extensions);
	}

	/**
	 * @param startPath
	 *            initial directory to come up with
	 * @param description
	 *            displayed to the user
	 * @param extensions
	 *            which are allowed
	 * @return the selected file or null is the operation is canceled
	 */
	public static File saveFile(File startPath, String description, String extensions[]) {
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle(description);
		chooser.setCurrentDirectory(startPath);
		String typeDesc = "";
		for (String t : extensions)
			typeDesc += t + " ";
		FileNameExtensionFilter filter =
			new FileNameExtensionFilter(typeDesc, extensions);
		chooser.setFileFilter(filter);
		int returnVal = chooser.showSaveDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			return chooser.getSelectedFile();
		} else
			return null;
	}

	/**
	 * @param filename
	 * @param content
	 * @return true if writing succeeded
	 */
	public static boolean writeToFile(String filename, String content) {
		File file = new File(filename);
		return writeToFile(file, content);
	}

	/**
	 * @param file
	 * @param content
	 * @return true if writing succeeded
	 */
	public static boolean writeToFile(File file, String content) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(content);
			writer.close();
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	/**
	 * @param startPath
	 *            initial directory to come up with
	 * @param description
	 *            displayed to the user
	 * @param extensions
	 *            which are allowed
	 * @return the selected file or null is the operation is canceled
	 */
	public static File requestFile(String startPath, String description,
		String extensions[]) {
		JFileChooser chooser = new JFileChooser(startPath);

		String typeDesc = "";
		for (String t : extensions)
			typeDesc += t + " ";

		FileNameExtensionFilter filter =
			new FileNameExtensionFilter(typeDesc, extensions);
		chooser.setFileFilter(filter);
		chooser.setDialogTitle(description);
		int returnVal = chooser.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			return chooser.getSelectedFile();
		} else
			return null;
	}

	/**
	 * @param source
	 * @param destination
	 * @return true if operation succeeded - false if exception occured
	 */
	public static boolean copyFile(String source, String destination) {
		return copyFile(new File(source), new File(destination));
	}

	/**
	 * @param f1
	 * @param f2
	 * @return true if operation succeeded - false if exception occured
	 */
	public static boolean copyFile(File f1, File f2) {
		try {
			InputStream in = new FileInputStream(f1);
			OutputStream out = new FileOutputStream(f2);

			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
			return true;
		} catch (FileNotFoundException ex) {
			return false;
		} catch (IOException e) {
			return false;
		}
	}

	/**
	 * @param source
	 * @param target
	 * @return true if operation succeeded - false if exception occured
	 */
	public static boolean copyDirectory(String source, String target) {
		return copyDirectory(new File(source), new File(target));
	}

	/**
	 * @param sourceLocation
	 * @param targetLocation
	 * @return true if operation succeeded - false if exception occured
	 */
	public static boolean copyDirectory(File sourceLocation, File targetLocation) {
		boolean ok = true;
		if (sourceLocation.isDirectory()) {
			if (!targetLocation.exists()) {
				targetLocation.mkdir();
			}

			String[] children = sourceLocation.list();
			for (int i = 0; i < children.length; i++) {
				copyDirectory(new File(sourceLocation, children[i]), new File(
					targetLocation, children[i]));
			}
		} else {
			boolean g = copyFile(sourceLocation, targetLocation);
			if (!g)
				ok = false;
		}
		return ok;
	}

	/**
	 * @param path
	 * @return true if operation succeeded - false if exception occured
	 */
	public static boolean deleteDirectory(File path) {
		if (path.exists()) {
			File[] files = path.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteDirectory(files[i]);
				} else {
					files[i].delete();
				}
			}
		}
		return (path.delete());
	}
}
