package tr.org.liderahenk.liderconsole.core.exceptions;

/**
 * REST exception is fired to notify REST request failed.
 * 
 * @author <a href="mailto:emre.akkaya@agem.com.tr">Emre Akkaya</a>
 *
 */
public class RestException extends Exception {

	private static final long serialVersionUID = -8355089653100461858L;

	public RestException() {
		super();
	}

	public RestException(final String message) {
		super(message);
	}

	public RestException(final Throwable throwable) {
		super(throwable);
	}

	public RestException(final String message, final Throwable throwable) {
		super(message, throwable);
	}

}
