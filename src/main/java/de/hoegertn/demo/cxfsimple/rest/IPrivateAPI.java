package de.hoegertn.demo.cxfsimple.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import de.hoegertn.demo.cxfsimple.auth.Roles;
import de.hoegertn.demo.cxfsimple.auth.UserRole;

public interface IPrivateAPI {
	
	@Path("/user")
	@GET
	@Roles({UserRole.User})
	void callUser();
	
	@Path("/admin")
	@GET
	@Roles({UserRole.Admin})
	void callAdmin();
	
}
