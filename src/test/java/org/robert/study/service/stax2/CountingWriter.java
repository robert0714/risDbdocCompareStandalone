package org.robert.study.service.stax2;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;

public class CountingWriter extends FilterWriter {
	long filtCount = 0;
	char filteredChar = 0;

	public CountingWriter(Writer out, char filter) {
		super(out);
		filteredChar = filter;
		filtCount = 0;
	}

	public long getCount() {
		return filtCount;
	}

	public void resetCount() {
		filtCount = 0;
	}

	public void write(int c) throws IOException {
		if (filteredChar == c) {
			// out.write(c);
			filtCount++;
		}
	}

	public void write(char[] cbuf, int off, int len) throws IOException {
		int idx = off;
		int count = 0;
		while (count < len) {
			write(cbuf[idx]);
			idx++;
			count++;
		}
	}

	public void write(String str, int off, int len) throws IOException {
		int idx = off;
		int count = 0;
		while (count < len) {
			write(str.charAt(idx));
			idx++;
			count++;
		}
	}
}
