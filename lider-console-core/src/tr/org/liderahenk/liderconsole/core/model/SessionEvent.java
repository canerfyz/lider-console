package tr.org.liderahenk.liderconsole.core.model;

/**
 * Session event indicating either login or logout.
 * 
 * @author <a href="mailto:emre.akkaya@agem.com.tr">Emre Kağan Akkaya</a>
 *
 */
public enum SessionEvent {

	LOGIN(1), LOGOUT(2);

	private int id;

	private SessionEvent(int id) {
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
	 * @return related SessionEvent enum
	 * @see http://blog.chris-ritchie.com/2013/09/mapping-enums-with-fixed-id-in
	 *      -jpa.html
	 * 
	 */
	public static SessionEvent getType(Integer id) {
		if (id == null) {
			return null;
		}
		for (SessionEvent position : SessionEvent.values()) {
			if (id.equals(position.getId())) {
				return position;
			}
		}
		throw new IllegalArgumentException("No matching type for id: " + id);
	}

}
