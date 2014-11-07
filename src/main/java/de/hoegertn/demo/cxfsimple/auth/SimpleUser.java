package de.hoegertn.demo.cxfsimple.auth;

import java.util.List;

public class SimpleUser {
	
	private String userName;
	private String firstName;
	private String lastName;
	private List<UserRole> roles;
	
	
	public String getUserName() {
		return this.userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getFirstName() {
		return this.firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return this.lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public List<UserRole> getRoles() {
		return this.roles;
	}
	
	public void setRoles(List<UserRole> roles) {
		this.roles = roles;
	}
	
	@Override
	public String toString() {
		return "SimpleUser [firstName=" + this.firstName + ", lastName=" + this.lastName + "]";
	}
	
}
