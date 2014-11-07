package de.hoegertn.demo.cxfsimple.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.hoegertn.demo.cxfsimple.rest.model.TestObject;

@Path("/demo")
public interface IDemoAPI {
	
	@Path("/{name}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	TestObject getObject(@PathParam("name") String name);
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	Response putObject(TestObject object);
	
}
