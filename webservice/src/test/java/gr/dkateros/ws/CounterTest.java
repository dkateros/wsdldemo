package gr.dkateros.ws;

import javax.xml.ws.BindingProvider;

/**
 * Stateful service client demo.
 * 
 * Use the same proxy instance after setting {@link BindingProvider#SESSION_MAINTAIN_PROPERTY} to true
 * on its request context for every call.
 * 
 * This is an end to end test that requires the web service to be actually running at localhost:9080,
 * hence not written as a unit test, since it will not succeed build time.
 */
public class CounterTest {
	
	public static void main(String[] args) {
		CounterService factory = new CounterService();
		Counter proxy = factory.getCounterPort();
		BindingProvider bp = (BindingProvider) proxy;
        bp.getRequestContext().put(BindingProvider.SESSION_MAINTAIN_PROPERTY, true);
        
        check(1, proxy.get());
        check(2, proxy.get());
        check(3, proxy.get());
        
        System.out.println("Success!");
	}
	
	static void check(Integer expected, Integer actual) {
		if(!expected.equals(actual)) { // no null check, what?? :D
			throw new RuntimeException(String.format("expected %d, but got %d instead", expected, actual));
		}
	}

}
