package com.auto.app.exception;

public class DocumentNotFoundException extends Exception {

	public DocumentNotFoundException() {
		super();
	}

	public DocumentNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public DocumentNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public DocumentNotFoundException(String message) {
		super(message);
	}

	public DocumentNotFoundException(Throwable cause) {
		super(cause);
	}
}
