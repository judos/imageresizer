package ch.judos.imageresizer.helper;

import java.io.IOException;
import java.io.Reader;

public class NonBlockingReader {

	private Reader reader;
	private StringBuffer buf;

	public NonBlockingReader(Reader reader) {
		this.reader = reader;
		this.buf = new StringBuffer();
	}

	public String nextLine() throws IOException {
		readAsMuchAsPossible();
		return tryToPopFinishedLine();
	}

	private String tryToPopFinishedLine() {
		synchronized (this.buf) {
			int newLinePos = this.buf.indexOf(System.lineSeparator());
			if (newLinePos == -1) {
				return null;
			}
			String result = this.buf.substring(0, newLinePos);
			this.buf.replace(0, newLinePos + System.lineSeparator().length(), "");
			return result;
		}
	}

	private void readAsMuchAsPossible() throws IOException {
		char[] tmp = new char[1024];
		if (reader.ready()) {
			synchronized (this.buf) {
				while (reader.ready()) {
					int i = reader.read(tmp, 0, 1024);
					if (i < 0)
						break;
					this.buf.append(new String(tmp, 0, i));
				}
			}
		}
	}

	public void close() throws IOException {
		this.reader.close();
	}

}
