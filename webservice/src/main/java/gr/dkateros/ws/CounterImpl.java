package gr.dkateros.ws;

import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

/**
 * A java first stateful web service. The service uses the HttpSession to store and retrieve state.
 * 
 * If the web service conversational scope is other than that of the HttpSession (e.g. multiple 
 * conversations may start from the same HttpSession), it is a good idea to provide semantics 
 * for discarding state. This could be achieved, for instance, by providing a web service method 
 * that discards the existing state.
 */
@WebService(
		name = "Counter",
		serviceName = "CounterService",
		portName = "CounterPort")
public class CounterImpl {

	private static final String CTR = "ctr";
	
	@Resource WebServiceContext ctx;
	
	@WebMethod
	public Integer get() {
		MessageContext mc = ctx.getMessageContext();
		HttpSession session = ((HttpServletRequest) mc.get(MessageContext.SERVLET_REQUEST)).getSession();
		Integer ctr = (Integer) session.getAttribute(CTR);
		if(ctr == null) {
			ctr = 0;
		}
		Integer result = ctr + 1;
		session.setAttribute(CTR, result);
		return result;
	}

}
