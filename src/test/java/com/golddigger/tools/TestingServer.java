package com.golddigger.tools;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

import com.golddigger.server.GoldDiggerServer;

public class TestingServer extends GoldDiggerServer {

	public String execute(String url){
		CacheWriter writer = new CacheWriter();
		this.process(url, new PrintWriter(writer));
		return writer.read();
	}

	private class CacheWriter extends Writer {
		private String data = "";

		@Override
		public void write(char[] cbuf, int off, int len) throws IOException {
			for (int i = 0; i < len; i++){
				data+=cbuf[off+i];
			}
		}

		public String read(){
			return this.data;
		}

		@Override
		public void flush() throws IOException { }

		@Override
		public void close() throws IOException { }

	}
}
