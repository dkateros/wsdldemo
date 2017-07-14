package gr.dkateros.ws.util.log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;

/**
 * {@link ServletOutputStream} decorator. Intercepts {@link #write(int)} to store the output to an internal byte array.
 */
public class RecordingServletOutputStream 
extends ServletOutputStream {
	
	private ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	
	private ServletOutputStream servletOutputStream;
	
	public RecordingServletOutputStream(ServletOutputStream servletOutputStream) {
		this.servletOutputStream = servletOutputStream;
	}
	
	public byte[] getPayload() {
		return byteArrayOutputStream.toByteArray();
	}
	
	@Override
	public void write(int b) throws IOException {
		byteArrayOutputStream.write(b);
		servletOutputStream.write(b);
	}
	
	@Override
	public void flush() throws IOException {
		servletOutputStream.flush();
	}
	
	@Override
	public void close() throws IOException {
		servletOutputStream.close();
	}
	
	@Override
	public boolean isReady() {
		return servletOutputStream.isReady();
	}

	@Override
	public void setWriteListener(WriteListener writeListener) {
		servletOutputStream.setWriteListener(writeListener);
	}
	
}
