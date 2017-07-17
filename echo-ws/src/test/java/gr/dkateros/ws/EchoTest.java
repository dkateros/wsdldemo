package gr.dkateros.ws;

import java.util.UUID;

import gr.dkateros.ws.util.client.SoapWsClient;

/**
 * Simple test on top of apache httpclient.
 * Needs a bit know how on how to construct the SOAP message, but we are
 * doing contract first web services anyway ;-).
 */
public class EchoTest {
	
	static String serviceEndPoint = "http://localhost:8080/echo/Echo";
	
	public static void main(String[] args) {
		String payload = UUID.randomUUID().toString();
		SoapWsClient client = new SoapWsClient();
		String message = "<EchoInput xmlns=\"http://www.dkateros.gr/ws/types\"><payload>" + payload + "</payload></EchoInput>";
		String response = client.execute(serviceEndPoint, "Echo", message);
		System.out.println(response);
		if(!response.contains(payload)) {
			throw new RuntimeException("failed");
		} else {
			System.out.println("Success!");
		}
	}
	

}
