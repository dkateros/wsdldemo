package gr.dkateros.ws;

import javax.xml.ws.BindingProvider;

/**
 * Stateful service client demo.
 * 
 * Use the same proxy instance after setting {@link BindingProvider#SESSION_MAINTAIN_PROPERTY} to true
 * on its request context for every call.
 */
public class CounterTest {
	
	public static void main(String[] args) {
		CounterService factory = new CounterService();
		Counter proxy = factory.getCounterPort();
		BindingProvider bp = (BindingProvider) proxy;
        bp.getRequestContext().put(BindingProvider.SESSION_MAINTAIN_PROPERTY, true);
        
        System.out.println(proxy.get()); //1
        System.out.println(proxy.get()); //2
        System.out.println(proxy.get()); //3
	}

}
