package tr.org.liderahenk.liderconsole.core.xmpp.enums;

/**
 * Types used when sending messages <b>from Lider to Lider Console</b>.<br/>
 * <br/>
 * 
 * @author <a href="mailto:emre.akkaya@agem.com.tr">Emre Akkaya</a>
 *
 */
public enum NotificationType {

	TASK(1), TASK_STATUS(2);

	private int id;

	private NotificationType(int id) {
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
	 * @return related LiderMessageType enum
	 * @see http://blog.chris-ritchie.com/2013/09/mapping-enums-with-fixed-id-in
	 *      -jpa.html
	 * 
	 */
	public static NotificationType getType(Integer id) {
		if (id == null) {
			return null;
		}
		for (NotificationType value : NotificationType.values()) {
			if (id.equals(value.getId())) {
				return value;
			}
		}
		throw new IllegalArgumentException("No matching type for id: " + id);
	}

}
