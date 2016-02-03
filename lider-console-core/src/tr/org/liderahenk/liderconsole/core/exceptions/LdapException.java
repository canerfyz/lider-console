package tr.org.liderahenk.liderconsole.core.exceptions;

/**
 * 
 * @author <a href="mailto:emre.akkaya@agem.com.tr">Emre Akkaya</a>
 *
 */
public class LdapException extends Exception {
				
	private static final long serialVersionUID = -552155632436157913L;

	public LdapException() {
		super();
	}

	public LdapException(final String message) {
		super(message);
	}

	public LdapException(final Throwable throwable) {
		super(throwable);
	}

	public LdapException(final String message, final Throwable throwable) {
		super(message, throwable);
	}

}
