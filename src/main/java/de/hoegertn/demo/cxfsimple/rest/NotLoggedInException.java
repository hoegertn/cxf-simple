package de.hoegertn.demo.cxfsimple.rest;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class NotLoggedInException extends NotAuthorizedException {
	
	private static final long serialVersionUID = 2283306178410888697L;
	
	
	public NotLoggedInException() {
		super(Response.status(Status.UNAUTHORIZED).entity("Invalid apikey").build());
	}
	
}
