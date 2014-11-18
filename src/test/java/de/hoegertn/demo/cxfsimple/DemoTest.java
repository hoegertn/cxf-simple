package de.hoegertn.demo.cxfsimple;

import org.apache.http.HttpResponse;
import org.junit.Assert;
import org.junit.Test;

import de.hoegertn.demo.cxfsimple.rest.model.TestObject;
import de.taimos.httputils.WS;

public class DemoTest extends BasicTest {
	
	@Test
	public void testNotExisting() {
		HttpResponse get = this.request("/public/demo/{name}").pathParam("name", "nx").get();
		System.out.println(get);
		Assert.assertEquals(404, WS.getStatus(get));
	}
	
	@Test
	public void testCreate() {
		TestObject t1 = new TestObject();
		t1.setName("foo");
		t1.setNumber(42);
		
		HttpResponse post = this.json(this.request("/public/demo"), t1).post();
		System.out.println(post);
		Assert.assertEquals(201, WS.getStatus(post));
		
		HttpResponse get = this.request("/public/demo/{name}").pathParam("name", "foo").get();
		System.out.println(get);
		Assert.assertEquals(200, WS.getStatus(get));
		TestObject parse = this.parse(WS.getResponseAsString(get), TestObject.class);
		Assert.assertNotNull(parse);
		Assert.assertEquals("foo", parse.getName());
		Assert.assertEquals(Integer.valueOf(42), parse.getNumber());
	}
	
	@Test
	public void testCreateDuplicate() {
		TestObject t1 = new TestObject();
		t1.setName("duplicate");
		t1.setNumber(42);
		
		HttpResponse post = this.json(this.request("/public/demo"), t1).post();
		System.out.println(post);
		Assert.assertEquals(201, WS.getStatus(post));
		
		HttpResponse post2 = this.json(this.request("/public/demo"), t1).post();
		System.out.println(post2);
		Assert.assertEquals(400, WS.getStatus(post2));
	}
}
