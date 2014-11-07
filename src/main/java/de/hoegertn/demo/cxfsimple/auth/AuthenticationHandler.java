/**
 *
 */
package de.hoegertn.demo.cxfsimple.auth;

import java.io.IOException;
import java.security.Principal;

import javax.security.auth.Subject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;

import org.apache.cxf.common.security.SimplePrincipal;
import org.apache.cxf.interceptor.security.DefaultSecurityContext;
import org.apache.cxf.jaxrs.impl.HttpHeadersImpl;
import org.apache.cxf.jaxrs.utils.JAXRSUtils;
import org.apache.cxf.message.Message;
import org.apache.cxf.security.SecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

/**
 * 
 * Copyright 2014 Hoegernet<br>
 * <br>
 * 
 * @author Thorsten Hoeger
 *
 */
public class AuthenticationHandler implements ContainerRequestFilter {
	
	private static final Logger logger = LoggerFactory.getLogger(AuthenticationHandler.class);
	
	
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		Message m = JAXRSUtils.getCurrentMessage();
		
		HttpHeaders head = new HttpHeadersImpl(m);
		String apiKey = head.getHeaderString("X-ApiKey");
		if ((apiKey != null) && !apiKey.isEmpty()) {
			
			// a DAO call should be used here
			final SimpleUser su;
			if (apiKey.equals("superKey")) {
				su = new SimpleUser();
				su.setUserName("jsmith");
				su.setFirstName("John");
				su.setLastName("Smith");
				su.setRoles(Lists.newArrayList(UserRole.Admin, UserRole.User));
			} else if (apiKey.equals("fooKey")) {
				su = new SimpleUser();
				su.setUserName("afoo");
				su.setFirstName("Alex");
				su.setLastName("Foo");
				su.setRoles(Lists.newArrayList(UserRole.User));
			} else {
				su = null;
			}
			if (su != null) {
				// put user data into context
				m.put(SecurityContext.class, AuthenticationHandler.createSC(su));
				m.put(SimpleUser.class, su);
				// RolesFilter uses different context map
				m.setContent(SimpleUser.class, su);
				AuthenticationHandler.logger.info("Logged in " + su);
			}
		}
	}
	
	private static SecurityContext createSC(final SimpleUser u) {
		final Subject subject = new Subject();
		
		final Principal principal = new SimplePrincipal(u.getUserName());
		subject.getPrincipals().add(principal);
		
		if (u.getRoles() != null) {
			for (final UserRole role : u.getRoles()) {
				subject.getPrincipals().add(new SimplePrincipal(role.toString()));
			}
		}
		return new DefaultSecurityContext(principal, subject);
	}
	
}
