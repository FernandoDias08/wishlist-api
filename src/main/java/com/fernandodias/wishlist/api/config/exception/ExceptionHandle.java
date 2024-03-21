package com.fernandodias.wishlist.api.config.exception;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.WebUtils;

@ControllerAdvice
public class ExceptionHandle extends ResponseEntityExceptionHandler {

	@ExceptionHandler({ WishlistException.class })
	public ResponseEntity<Object> customHandleException(WishlistException ex, WebRequest request) {
		String userMessage = ex.getMessage();
		String developerMessage = "An error occurred in the system: " + ExceptionUtils.getRootCauseMessage(ex);
		Error error = new Error(userMessage, developerMessage);
		return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY, request);
	}
	
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		super.logger.error("The operation could not be performed. Cause", ex);

		if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
			request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, RequestAttributes.SCOPE_REQUEST);
		}

		return new ResponseEntity<>(body, headers, status);
	}

	public static class Error {

		private String userMessage;
		private String developerMessage;

		public Error(String userMessage, String developerMessage) {
			this.userMessage = userMessage;
			this.developerMessage = developerMessage;
		}

		public String getUserMessage() {
			return userMessage;
		}

		public String getDeveloperMessage() {
			return developerMessage;
		}

	}

}
