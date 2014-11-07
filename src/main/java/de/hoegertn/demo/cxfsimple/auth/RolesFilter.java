package de.hoegertn.demo.cxfsimple.auth;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.cxf.jaxrs.utils.JAXRSUtils;
import org.apache.cxf.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;

public class RolesFilter implements ContainerRequestFilter {
	
	private static final Logger logger = LoggerFactory.getLogger(RolesFilter.class);
	
	
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		Message m = JAXRSUtils.getCurrentMessage();
		
		final Method method = (Method) m.get("org.apache.cxf.resource.method");
		final UserRole[] needed = this.searchRoles(method);
		if (needed.length == 0) {
			// No roles needed
			return;
		}
		
		RolesFilter.logger.info("Needs:" + Joiner.on(",").join(needed));
		
		SimpleUser user = m.get(SimpleUser.class);
		
		if (user != null) {
			List<UserRole> roles = user.getRoles();
			RolesFilter.logger.info("Has: " + Joiner.on(",").join(roles));
			
			for (final UserRole need : needed) {
				for (final UserRole role : roles) {
					if (role.equals(need)) {
						// Let it pass
						return;
					}
				}
			}
			String text = "Missing at least one of the following roles: " + Joiner.on(",").join(needed);
			requestContext.abortWith(Response.status(Status.FORBIDDEN).entity(text).build());
		} else {
			String text = "Not logged in";
			requestContext.abortWith(Response.status(Status.UNAUTHORIZED).entity(text).build());
		}
	}
	
	private UserRole[] searchRoles(Method method) {
		if (method == null) {
			return new UserRole[0];
		}
		if (method.isAnnotationPresent(Roles.class)) {
			return method.getAnnotation(Roles.class).value();
		}
		if (method.getDeclaringClass().getInterfaces().length != 0) {
			final Class<?>[] interfaces = method.getDeclaringClass().getInterfaces();
			final UserRole[] needs = this.searchClassArray(interfaces, method);
			if (needs.length > 0) {
				return needs;
			}
		}
		return new UserRole[0];
	}
	
	private UserRole[] searchClassArray(Class<?>[] classes, Method m) {
		for (final Class<?> iface : classes) {
			try {
				final Method iMeth = iface.getMethod(m.getName(), m.getParameterTypes());
				if (iMeth.isAnnotationPresent(Roles.class)) {
					return iMeth.getAnnotation(Roles.class).value();
				}
			} catch (NoSuchMethodException | SecurityException e) {
				// search next
			}
		}
		return new UserRole[0];
	}
}
