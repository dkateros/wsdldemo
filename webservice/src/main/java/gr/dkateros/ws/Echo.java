package gr.dkateros.ws;

import javax.jws.WebService;

import gr.dkateros.ws.types.SimpleMessage;

/**
 * A contract first stateless jaxws web service.
 */
@WebService(
		serviceName="Echo",
		portName="EchoPort",
		targetNamespace="http://www.dkateros.gr/ws",
		endpointInterface="gr.dkateros.ws.EchoPortType",
		wsdlLocation="gr/dkateros/ws/contract/echo.wsdl")
public class Echo implements EchoPortType {

	@Override
	public SimpleMessage echo(SimpleMessage echoInput) {
		return echoInput;
	}

}
