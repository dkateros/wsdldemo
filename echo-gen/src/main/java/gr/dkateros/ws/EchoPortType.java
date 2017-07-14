
package gr.dkateros.ws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;
import gr.dkateros.ws.echo.ObjectFactory;
import gr.dkateros.ws.echo.SimpleMessage;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.7-b01-
 * Generated source version: 2.1
 * 
 */
@WebService(name = "EchoPortType", targetNamespace = "http://www.dkateros.gr/ws")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
@XmlSeeAlso({
    ObjectFactory.class
})
public interface EchoPortType {


    /**
     * 
     * @param echoInput
     * @return
     *     returns gr.dkateros.ws.echo.SimpleMessage
     */
    @WebMethod(operationName = "Echo", action = "Echo")
    @WebResult(name = "EchoOutput", targetNamespace = "http://www.dkateros.gr/ws/echo", partName = "EchoOutput")
    public SimpleMessage echo(
        @WebParam(name = "EchoInput", targetNamespace = "http://www.dkateros.gr/ws/echo", partName = "EchoInput")
        SimpleMessage echoInput);

}
