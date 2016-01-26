package tr.org.liderahenk.liderconsole.core.task;

import java.io.Serializable;

public class RestRequest implements Serializable {

	private static final long serialVersionUID = 3105757484950626565L;

	private String resource;
	private String access;
	private String attribute;
	private String command;
	private String action;
	private String user;

	private RestRequestBody body;

	public RestRequest(String resource, String access, String attribute, String command, String action,
			RestRequestBody body) {
		this.resource = resource;
		this.access = access;
		this.attribute = attribute;
		this.command = command;
		this.action = action;
		this.body = body;
	}

	public String getResource() {
		return resource;
	}

	public String getAccess() {
		return access;
	}

	public String getAttribute() {
		return attribute;
	}

	public String getCommand() {
		return command;
	}

	public String getAction() {
		return action;
	}

	public RestRequestBody getBody() {
		return body;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
}