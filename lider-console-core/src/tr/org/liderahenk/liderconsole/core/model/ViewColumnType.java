package tr.org.liderahenk.liderconsole.core.model;

/**
 * View column type indicates if a column can be used as value or label field in
 * a chart.
 * 
 * @author <a href="mailto:emre.akkaya@agem.com.tr">Emre Kağan Akkaya</a>
 *
 */
public enum ViewColumnType {

	VALUE_FIELD(1), LABEL_FIELD(2);

	private int id;

	private ViewColumnType(int id) {
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
	 * @return related ViewColumnType enum
	 * @see http://blog.chris-ritchie.com/2013/09/mapping-enums-with-fixed-id-in
	 *      -jpa.html
	 * 
	 */
	public static ViewColumnType getType(Integer id) {
		if (id == null) {
			return null;
		}
		for (ViewColumnType type : ViewColumnType.values()) {
			if (id.equals(type.getId())) {
				return type;
			}
		}
		throw new IllegalArgumentException("No matching type for id: " + id);
	}

}
