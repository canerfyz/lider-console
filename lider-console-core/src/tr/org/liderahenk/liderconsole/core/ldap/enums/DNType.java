package tr.org.liderahenk.liderconsole.core.ldap.enums;

/**
 * This enum is used to indicate which DN entries to consider when processing a
 * request.
 * 
 * @author <a href="mailto:emre.akkaya@agem.com.tr">Emre Akkaya</a>
 *
 */
public enum DNType {
	AHENK(1), USER(2), GROUP(3), ALL(4), ORGANIZATIONAL_UNIT(5);

	private int id;

	private DNType(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public static DNType getType(Integer id) {
		if (id == null) {
			return null;
		}
		for (DNType position : DNType.values()) {
			if (id.equals(position.getId())) {
				return position;
			}
		}
		throw new IllegalArgumentException("No matching type for id: " + id);
	}
}
