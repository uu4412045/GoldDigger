package com.golddigger.utils.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class ContinuousFileReader extends BufferedReader{
	String buffer;
	public ContinuousFileReader(File file) throws IOException {
		this(new FileReader(file));
	}
	
	public ContinuousFileReader(Reader in) {
		super(in);
	}
	
	/**
	 * Should return null until there is an entire line to read.
	 */
	@Override
	public String readLine() throws IOException{
		String line = super.readLine();
		if (line != null && line.endsWith("\n")) {
			return line;
		} else {
			buffer += line;
			return null;
		}
	}
}