package gr.dkateros.ws.util.document;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamSource;

/**
 * Helps resolve xsd files referenced in a wsdl file. The wsdl {@link File} is
 * supplied construction time. The referenced xsd files are resolved using the
 * wsdl's file directory location.
 * 
 * @see Wsdl2Html
 */
public class RelativeFileUriResolver implements URIResolver {
	
	File wsdl;
	
	public RelativeFileUriResolver(File wsdl) {
		super();
		this.wsdl = wsdl;
	}

	public Source resolve(String href, String base) throws TransformerException {
		String path = wsdl.getParent() + File.separator + href;
		try {
			return new StreamSource(new FileInputStream(path));
		} catch (FileNotFoundException e) {
			throw new TransformerException(e);
		}
	}

}
