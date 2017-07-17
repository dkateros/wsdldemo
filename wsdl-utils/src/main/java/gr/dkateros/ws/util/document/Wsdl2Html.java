package gr.dkateros.ws.util.document;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.FileUtils;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

/**
 * Task implementation that converts WSDL files to HTML documentation files
 * using an existing XSL transformation.
 */
public class Wsdl2Html extends Task {

	/**
	 * Basic xsl, retrieved from http://tomi.vanek.sk
	 */
	static final String WSDL_XSLT = "gr/dkateros/ws/util/document/wsdl-viewer.xsl";
	static final String HTML_EXTENSION = "html";
	static final String WSDL_EXTENSION = "wsdl";
	
	/**
	 * Base dir to search for wsdl files.
	 */
	String sourceDir;
	
	/**
	 * Dir to output the generated HTML.
	 */
	String targetDir;

	@Override
	public void execute() throws BuildException {
		
		try {
			File out = new File(targetDir);
			FileUtils.forceMkdir(out);
			FileUtils.cleanDirectory(out);

			TransformerFactory factory = TransformerFactory.newInstance();

			InputStream xsl = Thread.currentThread().getContextClassLoader().getResourceAsStream(WSDL_XSLT);

			Templates template = factory.newTemplates(new StreamSource(xsl));
			
			System.out.println(template);
			

			Transformer transformer = template.newTransformer();

			Collection<File> wsdls = FileUtils.listFiles(new File(sourceDir), new String[] { WSDL_EXTENSION }, true);

			for (File wsdl : wsdls) {
				System.out.println(wsdl);
				Source source = new StreamSource(new FileInputStream(wsdl));
				String outputFileName = wsdl.getName().replace(WSDL_EXTENSION, HTML_EXTENSION);
				Result result = new StreamResult(new FileOutputStream(targetDir + File.separator + outputFileName));

				transformer.setURIResolver(new RelativeFileUriResolver(wsdl));
				transformer.transform(source, result);
			}
		} catch (IOException | TransformerException e) {
			throw new BuildException(e);
		}

	}

	public void setSourceDir(String sourceDir) {
		this.sourceDir = sourceDir;
	}

	public void setTargetDir(String targetDir) {
		this.targetDir = targetDir;
	}
	
}
