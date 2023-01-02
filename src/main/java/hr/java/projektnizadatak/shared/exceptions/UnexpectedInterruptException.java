package hr.java.projektnizadatak.shared.exceptions;

public class UnexpectedInterruptException extends RuntimeException {
	public UnexpectedInterruptException() {
	}

	public UnexpectedInterruptException(String message) {
		super(message);
	}

	public UnexpectedInterruptException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnexpectedInterruptException(Throwable cause) {
		super(cause);
	}
}
