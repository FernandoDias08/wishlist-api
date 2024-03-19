package com.fernandodias.wishlist.api.config.exception;

public class WishlistException extends RuntimeException {

	private static final long serialVersionUID = 3021481041858928711L;

	public WishlistException() {
		super();
	}

	public WishlistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public WishlistException(String message, Throwable cause) {
		super(message, cause);
	}

	public WishlistException(String message) {
		super(message);
	}

	public WishlistException(Throwable cause) {
		super(cause.getMessage());
	}

}
