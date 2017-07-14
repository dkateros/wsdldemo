package gr.dkateros.ws.util.log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;

/**
 * {@link ServletInputStream} decorator. Intercepts {@link #read()} to store the input to an internal byte array.
 */
public class RecordingServletInputStream 
extends ServletInputStream {
	
	private ByteArrayOutputStream baos = new ByteArrayOutputStream();
	
	private ServletInputStream servletInputStream;
	
	public RecordingServletInputStream(ServletInputStream servletInputStream) {
		this.servletInputStream = servletInputStream;
	}
	
	public byte[] getPayload() {
		return baos.toByteArray();
	}
	
	@Override
	public int read() throws IOException {
		int ch = servletInputStream.read();
        if (ch != -1) {
        	baos.write(ch);
        }
        return ch;
	}
	
	@Override
	public int available() throws IOException {
		return servletInputStream.available();
	}
	
	@Override
	public synchronized void mark(int readlimit) {
		servletInputStream.mark(readlimit);
	}
	
	@Override
	public long skip(long n) throws IOException {
		return servletInputStream.skip(n);
	}
	
	@Override
	public void close() throws IOException {
		servletInputStream.close();
	}
	
	@Override
	public synchronized void reset() throws IOException {
		servletInputStream.reset();
	}
	
	@Override
	public boolean markSupported() {
		return servletInputStream.markSupported();
	}
	
	@Override
	public boolean isFinished() {
		return servletInputStream.isFinished();
	}

	@Override
	public boolean isReady() {
		return servletInputStream.isReady();
	}

	@Override
	public void setReadListener(ReadListener readListener) {
		servletInputStream.setReadListener(readListener);
	}
	
}
