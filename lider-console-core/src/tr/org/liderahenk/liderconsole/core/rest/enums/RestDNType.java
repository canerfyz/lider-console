package tr.org.liderahenk.liderconsole.core.rest.enums;

/**
 * This enum is used to indicate which DN entries to consider when processing a
 * request.
 * 
 * @author <a href="mailto:emre.akkaya@agem.com.tr">Emre Akkaya</a>
 *
 */
public enum RestDNType {
	AHENK(1), USER(2), GROUP(3), ALL(4);

	private int id;

	private RestDNType(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public static RestDNType getType(Integer id) {
		if (id == null) {
			return null;
		}
		for (RestDNType position : RestDNType.values()) {
			if (id.equals(position.getId())) {
				return position;
			}
		}
		throw new IllegalArgumentException("No matching type for id: " + id);
	}
}
