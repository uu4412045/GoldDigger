package com.golddigger.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

public class TestWriter extends Writer {
	StringBuffer buffer = new StringBuffer();
	PrintWriter printer;
	
	
	@Override
	public void write(char[] cbuf, int off, int len) throws IOException {
		buffer.append(cbuf, off, len);
	}

	@Override
	public void flush() throws IOException {}

	@Override
	public void close() throws IOException {}
	
	public String getHistory(){
		return buffer.toString();
	}
	
	public void clear(){
		buffer = new StringBuffer();
	}
	
	public PrintWriter getPrintWriter(){
		if (printer == null) printer = new PrintWriter(this);
		return printer;
	}
}
