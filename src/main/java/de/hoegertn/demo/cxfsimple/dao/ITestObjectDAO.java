package de.hoegertn.demo.cxfsimple.dao;

import de.hoegertn.demo.cxfsimple.rest.model.TestObject;

public interface ITestObjectDAO {
	
	/**
	 * @param name the name to search for
	 * @return the object or <code>null</code> if none found
	 */
	TestObject findByName(String name);
	
	/**
	 * @param object the object to save
	 * @return <code>true</code> if this object was saved or <code>false</code> if it already existed
	 */
	boolean save(TestObject object);
	
}
