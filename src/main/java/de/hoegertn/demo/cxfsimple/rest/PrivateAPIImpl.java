package de.hoegertn.demo.cxfsimple.rest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.cxf.jaxrs.ext.MessageContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hoegertn.demo.cxfsimple.auth.SimpleUser;

public class PrivateAPIImpl implements IContextAware, IPrivateAPI {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PrivateAPIImpl.class);
	
	protected MessageContext context;
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	
	
	@Override
	public void setMessageContext(MessageContext context) {
		this.context = context;
	}
	
	@Override
	public void setHttpServletRequest(HttpServletRequest request) {
		this.request = request;
	}
	
	@Override
	public void setHttpServletResponse(HttpServletResponse response) {
		this.response = response;
	}
	
	protected SimpleUser getLoggedIn() {
		return (SimpleUser) this.context.get(SimpleUser.class.getName());
	}
	
	protected void assertLoggedIn() {
		if (!this.isLoggedIn()) {
			throw new NotLoggedInException();
		}
	}
	
	protected boolean isLoggedIn() {
		return this.getLoggedIn() != null;
	}
	
	@Override
	public void callUser() {
		this.assertLoggedIn();
		
		PrivateAPIImpl.LOGGER.info("User: " + this.getLoggedIn());
	}
	
	@Override
	public void callAdmin() {
		this.assertLoggedIn();
		
		PrivateAPIImpl.LOGGER.info("Admin: " + this.getLoggedIn());
	}
}
