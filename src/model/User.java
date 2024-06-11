package model;
public class User {
	private String id;
	private String name;
	private String password;
	private USERROLE role;

	public static enum USERROLE {
		STAFF, MANAGER
	}

	public User(String id, String name, String password, USERROLE role) {
		this.id=id;
		this.name=name;
		this.password=password;
		this.role=role;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public USERROLE getRole() {
		return role;
	}

	public void setRole(USERROLE role) {
		this.role = role;
	}

}
