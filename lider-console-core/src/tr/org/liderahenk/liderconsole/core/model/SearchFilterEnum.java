package tr.org.liderahenk.liderconsole.core.model;

public enum SearchFilterEnum {

	EQ("="), NOT_EQ("!="), GT(">"), LT("<");

	private String operator;

	private SearchFilterEnum(String operator) {
		this.operator = operator;
	}

	public String getOperator() {
		return operator;
	}

	/**
	 * Provide mapping enums with a fixed ID in JPA (a more robust alternative
	 * to EnumType.String and EnumType.Ordinal)
	 * 
	 * @param id
	 * @return related SearchFilterEnum enum
	 * @see http://blog.chris-ritchie.com/2013/09/mapping-enums-with-fixed-id-in
	 *      -jpa.html
	 * 
	 */
	public static SearchFilterEnum getType(String operator) {
		if (operator == null) {
			return null;
		}
		for (SearchFilterEnum type : SearchFilterEnum.values()) {
			if (operator.equals(type.getOperator())) {
				return type;
			}
		}
		throw new IllegalArgumentException("No matching type for operator: " + operator);
	}
	
	public static String[] getOperators() {
		SearchFilterEnum[] values = SearchFilterEnum.values();
		String[] operators = new String[values.length];
		for (int i = 0; i < values.length; i++) {
			operators[i] = values[i].getOperator();
		}
		return operators;
	}

}
