package tr.org.liderahenk.liderconsole.core.exceptions;

public class ValidationException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public ValidationException() {
		super();
	}

	public ValidationException(final String message) {
		super(message);
	}

	public ValidationException(final Throwable throwable) {
		super(throwable);
	}

	public ValidationException(final String message, final Throwable throwable) {
		super(message, throwable);
	}
}
