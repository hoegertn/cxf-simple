package de.hoegertn.demo.cxfsimple;

import javax.ws.rs.core.MediaType;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.hoegertn.demo.cxfsimple.SpringDaemonTestRunner.RunnerConfiguration;
import de.taimos.httputils.HTTPRequest;
import de.taimos.httputils.WS;

@RunWith(SpringDaemonTestRunner.class)
@RunnerConfiguration(beanFile = "spring/beans.xml")
public abstract class BasicTest {
	
	@Autowired
	@Qualifier("objectMapper")
	private ObjectMapper mapper;
	
	
	/**
	 * put the given object as JSON to the given request and set the content type to "application/json"
	 * 
	 * @param req the {@link HTTPRequest}
	 * @param obj the object to serialize
	 * @return the {@link HTTPRequest} for fluent usage
	 */
	protected HTTPRequest json(HTTPRequest req, Object obj) {
		try {
			this.mapper = MapperFactory.createDefault();
			return req.contentType(MediaType.APPLICATION_JSON).body(this.mapper.writeValueAsString(obj));
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Create a {@link HTTPRequest} with the given path
	 * 
	 * @param path the URI path
	 * @return {@link HTTPRequest} with the given path for localhost server
	 */
	protected HTTPRequest request(String path) {
		return WS.url("http://localhost:8080" + path);
	}
	
	/**
	 * parse the given JSON string into a new instance of the given class
	 * 
	 * @param <T> the type to parse into
	 * @param json the JSON string
	 * @param targetClass the type to parse into
	 * @return the parsed object
	 */
	protected <T> T parse(String json, Class<T> targetClass) {
		try {
			return this.mapper.readValue(json, targetClass);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}