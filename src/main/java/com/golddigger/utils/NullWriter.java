package com.golddigger.utils;

import java.io.IOException;
import java.io.Writer;

public class NullWriter extends Writer {
	public static final NullWriter INSTANCE = new NullWriter();
	
	@Override
	public void write(char[] cbuf, int off, int len) throws IOException {
		return;
	}

	@Override
	public void flush() throws IOException {
		return;
	}

	@Override
	public void close() throws IOException {
		return;
	}

}
