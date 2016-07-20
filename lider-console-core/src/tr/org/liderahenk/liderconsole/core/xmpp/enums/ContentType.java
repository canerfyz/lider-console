package tr.org.liderahenk.liderconsole.core.xmpp.enums;

import java.util.Arrays;
import java.util.List;

/**
 * Common content types used to indicate type of the stored content in the
 * database.
 * 
 * @author <a href="mailto:emre.akkaya@agem.com.tr">Emre Kağan Akkaya</a>
 *
 */
public enum ContentType {

	APPLICATION_JSON(1), APPLICATION_PDF(2), APPLICATION_VND_MS_EXCEL(3), APPLICATION_MS_WORD(4), TEXT_PLAIN(
			5), TEXT_HTML(6), IMAGE_PNG(7), IMAGE_JPEG(8);

	private int id;

	private ContentType(int id) {
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
	 * @return related ContentType enum
	 * @see http://blog.chris-ritchie.com/2013/09/mapping-enums-with-fixed-id-in
	 *      -jpa.html
	 * 
	 */
	public static ContentType getType(Integer id) {
		if (id == null) {
			return null;
		}
		for (ContentType position : ContentType.values()) {
			if (id.equals(position.getId())) {
				return position;
			}
		}
		throw new IllegalArgumentException("No matching type for id: " + id);
	}

	public static List<ContentType> getFileContentTypes() {
		return Arrays.asList(new ContentType[] { APPLICATION_PDF, APPLICATION_VND_MS_EXCEL, APPLICATION_MS_WORD,
				IMAGE_PNG, IMAGE_JPEG, TEXT_PLAIN, TEXT_HTML });
	}

}
