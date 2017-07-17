package gr.dkateros.ws.util.client;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * Generic SOAP ws client with apache httpclient.
 */
public class SoapWsClient {

	/**
	 * Execute a SOAP action with the specified message on the specified service. Get the response. 
	 * 
	 * @param url Service end point.
	 * @param action Soap action.
	 * @param message Soap body.
	 * 
	 * @return Response body.
	 */
	public String execute(String url, String action, String message) {
		String response = null;

		try {
			String body = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ns1=\"http://example.com/v1.0/Records\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\" SOAP-ENV:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"><SOAP-ENV:Body>"
					+ message + "</SOAP-ENV:Body></SOAP-ENV:Envelope>";
			
			StringEntity stringEntity = new StringEntity(body, "UTF-8");
			stringEntity.setChunked(true);

			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(stringEntity);
			httpPost.addHeader("Accept", "text/xml");
			httpPost.addHeader("SOAPAction", action);

			HttpClient httpClient = new DefaultHttpClient();
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity entity = httpResponse.getEntity();

			if (entity != null) {
				response = EntityUtils.toString(entity);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return response;
	}

}
