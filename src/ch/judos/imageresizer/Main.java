package ch.judos.imageresizer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import ch.judos.imageresizer.controller.ScaleThread;
import ch.judos.imageresizer.helper.NonBlockingReader;
import ch.judos.imageresizer.model.FileDrop;
import ch.judos.imageresizer.model.FileDrop.Listener;
import ch.judos.imageresizer.model.IFrameModel;
import ch.judos.imageresizer.view.IFrame;

/**
 * TODO: show filename while progressing<br>
 * TODO: make it possible to add tasks while it is processing already<br>
 * TODO: an animation while processing, so the user knows its doing something
 */
public class Main implements Listener {

	public static void main(String[] args)
		throws IOException, InterruptedException, magick.MagickException {
		new Main();
	}

	private boolean running;

	private void scanInputToProcess(Process process)
		throws IOException, InterruptedException {
		BufferedWriter writer =
			new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
		NonBlockingReader scan =
			new NonBlockingReader(new BufferedReader(new InputStreamReader(System.in)));
		this.running = true;
		while (this.running) {
			String line = scan.nextLine();
			if (line == null) {
				Thread.sleep(1);
				continue;
			}
			writer.write(line);
			writer.newLine();
			writer.write("echo \"CMD_ENDED\"");
			writer.newLine();
			writer.flush();
		}
		scan.close();
		writer.close();
	}

	private void readOutput(Process process, Thread input) throws IOException {
		BufferedReader reader =
			new BufferedReader(new InputStreamReader(process.getInputStream()));
		String line;
		while ((line = reader.readLine()) != null) {
			System.out.println(line);
		}

		System.out.println("process has finished");
		this.running = false;
		reader.close();
	}

	private IFrameModel frame;

	public Main() throws IOException {

		int cores = Runtime.getRuntime().availableProcessors();
		System.out.println("Cores: " + cores);

		String command = "cmd";

		Process process = Runtime.getRuntime().exec(command);
		Thread input = new Thread(() -> {
			try {
				scanInputToProcess(process);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		});
		input.start();

		new Thread(() -> {
			try {
				readOutput(process, input);
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}).start();

		// initFrame();
	}

	private void initFrame() {
		IFrame f = new IFrame(true);
		new FileDrop(f, this);
		this.frame = f;
	}

	public void filesDropped(File[] files) {
		System.out.println("start conversion");
		if (!this.frame.allInputValid())
			return;

		Thread t = new ScaleThread(this.frame, files);
		t.start();
	}

}
