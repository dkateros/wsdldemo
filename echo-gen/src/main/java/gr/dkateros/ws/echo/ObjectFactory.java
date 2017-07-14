
package gr.dkateros.ws.echo;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the gr.dkateros.ws.echo package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _EchoOutput_QNAME = new QName("http://www.dkateros.gr/ws/echo", "EchoOutput");
    private final static QName _EchoInput_QNAME = new QName("http://www.dkateros.gr/ws/echo", "EchoInput");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: gr.dkateros.ws.echo
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SimpleMessage }
     * 
     */
    public SimpleMessage createSimpleMessage() {
        return new SimpleMessage();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SimpleMessage }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.dkateros.gr/ws/echo", name = "EchoOutput")
    public JAXBElement<SimpleMessage> createEchoOutput(SimpleMessage value) {
        return new JAXBElement<SimpleMessage>(_EchoOutput_QNAME, SimpleMessage.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SimpleMessage }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.dkateros.gr/ws/echo", name = "EchoInput")
    public JAXBElement<SimpleMessage> createEchoInput(SimpleMessage value) {
        return new JAXBElement<SimpleMessage>(_EchoInput_QNAME, SimpleMessage.class, null, value);
    }

}
