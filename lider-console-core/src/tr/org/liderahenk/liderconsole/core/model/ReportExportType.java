package tr.org.liderahenk.liderconsole.core.model;

import tr.org.liderahenk.liderconsole.core.i18n.Messages;

public enum ReportExportType {

	DISPLAY_TABLE(1), PDF_FILE(2)/*, HTML_FILE(3)*/;

	private int id;

	private ReportExportType(int id) {
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
	 * @return related ReportType enum
	 * @see http://blog.chris-ritchie.com/2013/09/mapping-enums-with-fixed-id-in
	 *      -jpa.html
	 * 
	 */
	public static ReportType getType(Integer id) {
		if (id == null) {
			return null;
		}
		for (ReportType type : ReportType.values()) {
			if (id.equals(type.getId())) {
				return type;
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
