package gr.dkateros.ws.util.log;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * {@link Filter} for SOAP based web services exposed as {@link Servlet}s.
 * <br/>
 * This implementation:
 * <li>does not use any logging facility, just the standard output.</li>
 * <li>assumes the request/response payload is an XML document encoded as UTF-8.</li>
 */
public class SoapLogger implements Filter {
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		//EMPTY
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		ServletRequest requestWrapper = request;
		ServletResponse responseWrapper = response;
		
		byte[] requestPayload = null;
		byte[] responsePayload = null;
		
		String url = null;
		
		if (request instanceof HttpServletRequest) {
			requestWrapper = new RecordingServletRequestWrapper((HttpServletRequest) request);
			url = ((HttpServletRequest) request).getRequestURL().toString();
		}

		if (response instanceof HttpServletResponse) {
			responseWrapper = new RecordingServletResponseWrapper((HttpServletResponse) response);
		}
		
		chain.doFilter(requestWrapper, responseWrapper);
		
		if(requestWrapper instanceof RecordingServletRequestWrapper) {
			requestPayload = ((RecordingServletRequestWrapper) requestWrapper).getPayload();
		}
		
		if(responseWrapper instanceof RecordingServletResponseWrapper) {
			responsePayload = ((RecordingServletResponseWrapper) responseWrapper).getPayload();
		}
		
		String msg = new StringBuilder().
				append("REQUEST " + url + "\n").
				append(soap(requestPayload)).
				append("\nRESPONSE\n").
				append(soap(responsePayload)).
				append("\n-------------------------------------------------------------------------------\n").
				toString();
		
		System.out.println(msg);
	}

	@Override
	public void destroy() {
		//EMPTY
	}
	
	String soap(byte[] payload) {
		ByteArrayInputStream bis = new ByteArrayInputStream(payload);
		StringWriter sw = new StringWriter();
		
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document document = builder.parse(bis);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.transform(new DOMSource(document), new StreamResult(sw));
		} catch (SAXException | IOException | ParserConfigurationException | TransformerException e) {
			throw new RuntimeException(e);
		}
		
		return sw.toString();
	}

}
