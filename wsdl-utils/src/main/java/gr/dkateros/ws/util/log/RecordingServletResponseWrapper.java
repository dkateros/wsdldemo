package gr.dkateros.ws.util.log;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * {@link HttpServletResponseWrapper} that facilitates recording of the {@link ServletOutputStream}
 * 
 * @see RecordingServletOutputStream
 */
public class RecordingServletResponseWrapper 
extends HttpServletResponseWrapper {

	RecordingServletOutputStream servletOutputStream;
	
	public RecordingServletResponseWrapper(HttpServletResponse response) throws IOException {
		super(response);
		this.servletOutputStream = new RecordingServletOutputStream(response.getOutputStream());
	}
	
	@Override
    public ServletOutputStream getOutputStream() throws IOException {
       return servletOutputStream;
    }
	
	public byte[] getPayload() {
		return servletOutputStream.getPayload();
	}
	
}
