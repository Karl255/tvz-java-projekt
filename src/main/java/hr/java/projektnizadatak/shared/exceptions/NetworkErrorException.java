package hr.java.projektnizadatak.shared.exceptions;

public class NetworkErrorException extends Exception {
	public NetworkErrorException() {
	}

	public NetworkErrorException(String message) {
		super(message);
	}

	public NetworkErrorException(String message, Throwable cause) {
		super(message, cause);
	}

	public NetworkErrorException(Throwable cause) {
		super(cause);
	}
}
