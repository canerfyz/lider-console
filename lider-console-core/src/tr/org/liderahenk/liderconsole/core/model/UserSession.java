package tr.org.liderahenk.liderconsole.core.model;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserSession implements Serializable {

	private static final long serialVersionUID = 4461189209173374369L;

	private Long id;

	private String username;

	private SessionEvent sessionEvent;

	private Date createDate;

	public UserSession() {
	}

	public UserSession(Long id, String username, SessionEvent sessionEvent, Date createDate) {
		super();
		this.id = id;
		this.username = username;
		this.sessionEvent = sessionEvent;
		this.createDate = createDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public SessionEvent getSessionEvent() {
		return sessionEvent;
	}

	public void setSessionEvent(SessionEvent sessionEvent) {
		this.sessionEvent = sessionEvent;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

}
