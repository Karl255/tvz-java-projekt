package hr.java.projektnizadatak.shared.exceptions;

public class DataNoLongerValidException extends Exception {
	public DataNoLongerValidException() {
	}

	public DataNoLongerValidException(String message) {
		super(message);
	}

	public DataNoLongerValidException(String message, Throwable cause) {
		super(message, cause);
	}

	public DataNoLongerValidException(Throwable cause) {
		super(cause);
	}
}
