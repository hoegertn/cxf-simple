package de.hoegertn.demo.cxfsimple.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import de.hoegertn.demo.cxfsimple.rest.model.TestObject;

@Repository
public class TestObjectMapDAO implements ITestObjectDAO {
	
	// Here we would use hibernate or something like this
	private Map<String, TestObject> objects = new HashMap<String, TestObject>();
	
	
	@Override
	public TestObject findByName(String name) {
		if (this.objects.containsKey(name)) {
			return this.objects.get(name);
		}
		return null;
	}
	
	@Override
	public boolean save(TestObject object) {
		if (this.objects.containsKey(object.getName())) {
			return false;
		}
		this.objects.put(object.getName(), object);
		return true;
	}
	
}
