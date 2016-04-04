package tr.org.liderahenk.liderconsole.core.xmpp.enums;

import tr.org.liderahenk.liderconsole.core.i18n.Messages;

/**
 * Status code used throughout the system. These status codes can be used in
 * (XMPP or REST) all messaging mechanisms.
 * 
 * <b>REGISTERED</b>: registration successful, agent IS registered.<br/>
 * <b>REGISTERED_WITHOUT_LDAP</b>: registered only for XMPP server, LDAP
 * registration awaiting further messaging.<br/>
 * <b>ALREADY_EXISTS</b>: agent node already exists in system, agent NOT
 * registered.<br/>
 * <b>REGISTRATION_ERROR</b>: registration error, agent NOT registered.<br/>
 * 
 * @author <a href="mailto:emre.akkaya@agem.com.tr">Emre Akkaya</a>
 *
 * 
 */
public enum StatusCode {

	REGISTERED(1), REGISTERED_WITHOUT_LDAP(2), ALREADY_EXISTS(3), REGISTRATION_ERROR(4), TASK_RECEIVED(
			5), TASK_PROCESSED(6), TASK_WARNING(7), TASK_ERROR(8), TASK_TIMEOUT(9), TASK_KILLED(10), POLICY_RECEIVED(
					11), POLICY_PROCESSED(12), POLICY_WARNING(13), POLICY_ERROR(14), POLICY_TIMEOUT(15), POLICY_KILLED(
							16);

	private int id;

	private StatusCode(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	/**
	 * Provide mapping enums with a fixed ID in JPA (a more robust alternative
	 * to EnumType.String and EnumType.Ordinal)
	 * 
	 * @param id
	 * @return related StatusCode enum
	 * @see http://blog.chris-ritchie.com/2013/09/mapping-enums-with-fixed-id-in
	 *      -jpa.html
	 * 
	 */
	public static StatusCode getType(Integer id) {
		if (id == null) {
			return null;
		}
		for (StatusCode position : StatusCode.values()) {
			if (id.equals(position.getId())) {
				return position;
			}
		}
		throw new IllegalArgumentException("No matching type for id: " + id);
	}

	/**
	 * Provide i18n message representation of the enum type.
	 * 
	 * @return
	 */
	public String getMessage() {
		return Messages.getString(this.toString());
	}

}
