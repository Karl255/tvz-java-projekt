package hr.java.projektnizadatak.shared.exceptions;

public class NoAdminUserException extends Exception {
	public NoAdminUserException() {
	}

	public NoAdminUserException(String message) {
		super(message);
	}

	public NoAdminUserException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoAdminUserException(Throwable cause) {
		super(cause);
	}
}
