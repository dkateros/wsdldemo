package gr.dkateros.ws.util.log;

import java.io.IOException;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * {@link HttpServletRequestWrapper} that facilitates recording of the {@link ServletInputStream}
 * 
 * @see RecordingServletInputStream
 */
public class RecordingServletRequestWrapper 
extends HttpServletRequestWrapper {

	RecordingServletInputStream servletInputStream;
	
	public RecordingServletRequestWrapper(HttpServletRequest request) throws IOException {
		super(request);
		this.servletInputStream = new RecordingServletInputStream(request.getInputStream());
	}
	
	@Override
    public ServletInputStream getInputStream() throws IOException {
       return servletInputStream;
    }
	
	public byte[] getPayload() {
		return servletInputStream.getPayload();
	}
	
}
