package de.hoegertn.demo.cxfsimple.rest;

import java.net.URI;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;

import de.hoegertn.demo.cxfsimple.dao.ITestObjectDAO;
import de.hoegertn.demo.cxfsimple.rest.model.TestObject;

public class DemoAPIImpl implements IDemoAPI {
	
	@Autowired
	private ITestObjectDAO dao;
	
	
	@Override
	public TestObject getObject(String name) {
		TestObject obj = this.dao.findByName(name);
		if (obj != null) {
			return obj;
		}
		throw new NotFoundException();
	}
	
	@Override
	public Response putObject(TestObject object) {
		if (this.dao.save(object)) {
			return Response.created(URI.create("/demo/" + object.getName())).build();
		}
		throw new BadRequestException("Object already existing");
	}
	
}
