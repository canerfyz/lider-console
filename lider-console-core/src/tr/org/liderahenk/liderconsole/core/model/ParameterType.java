package tr.org.liderahenk.liderconsole.core.model;

public enum ParameterType {
	STRING(1), DATE(2), NUMBER(3);

	private int id;

	private ParameterType(int id) {
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
	 * @return related CrudType enum
	 * @see http://blog.chris-ritchie.com/2013/09/mapping-enums-with-fixed-id-in
	 *      -jpa.html
	 * 
	 */
	public static ParameterType getType(Integer id) {
		if (id == null) {
			return null;
		}
		for (ParameterType paramType : ParameterType.values()) {
			if (id.equals(paramType.getId())) {
				return paramType;
			}
		}
		throw new IllegalArgumentException("No matching type for id: " + id);
	}
}
