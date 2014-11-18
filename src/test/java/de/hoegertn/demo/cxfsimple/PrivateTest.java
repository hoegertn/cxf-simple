package de.hoegertn.demo.cxfsimple;

import org.apache.http.HttpResponse;
import org.junit.Assert;
import org.junit.Test;

import de.taimos.httputils.WS;

public class PrivateTest extends BasicTest {
	
	@Test
	public void testNotLoggedIn() {
		HttpResponse get = this.request("/private/user").get();
		System.out.println(get);
		Assert.assertEquals(401, WS.getStatus(get));
		
		HttpResponse get2 = this.request("/private/admin").get();
		System.out.println(get2);
		Assert.assertEquals(401, WS.getStatus(get2));
	}
	
	@Test
	public void testUser() {
		HttpResponse get = this.request("/private/user").header("X-ApiKey", "fooKey").get();
		System.out.println(get);
		Assert.assertEquals(204, WS.getStatus(get));
		
		HttpResponse get2 = this.request("/private/admin").header("X-ApiKey", "fooKey").get();
		System.out.println(get2);
		Assert.assertEquals(403, WS.getStatus(get2));
	}
	
	@Test
	public void testAdmin() {
		HttpResponse get = this.request("/private/user").header("X-ApiKey", "superKey").get();
		System.out.println(get);
		Assert.assertEquals(204, WS.getStatus(get));
		
		HttpResponse get2 = this.request("/private/admin").header("X-ApiKey", "superKey").get();
		System.out.println(get2);
		Assert.assertEquals(204, WS.getStatus(get2));
	}
}
