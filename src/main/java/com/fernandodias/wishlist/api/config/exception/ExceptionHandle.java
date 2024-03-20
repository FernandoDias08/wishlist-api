package com.fernandodias.wishlist.api.config.exception;

import java.util.NoSuchElementException;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.WebUtils;

@ControllerAdvice
public class ExceptionHandle extends ResponseEntityExceptionHandler {

	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, WebRequest request) {

		String userMessage = "Invalid message";
		String developerMessage = ex.getCause() != null ? ex.getCause().toString() : ex.toString();
		Error error = new Error(userMessage, developerMessage);
		return handleExceptionInternal(ex, error, headers, HttpStatus.BAD_REQUEST, request);
	}

	@ExceptionHandler({ EmptyResultDataAccessException.class })
	public ResponseEntity<Object> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex,
			WebRequest request) {
		String userMessage = "Resource not found.";
		String developerMessage = ex.toString();
		Error error = new Error(userMessage, developerMessage);
		return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}

	@ExceptionHandler({ DataIntegrityViolationException.class })
	public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex,
			WebRequest request) {
		String userMessage = "Operation not permitted.";
		String developerMessage = ex.getMessage();
		Error error = new Error(userMessage, developerMessage);
		return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

	@ExceptionHandler(NoSuchElementException.class)
	public ResponseEntity<Object> customHandleException(NoSuchElementException ex, WebRequest request) {
		String userMessage = "Object does not exist or not found.";
		String developerMessage = ex.getMessage();
		Error error = new Error(userMessage, developerMessage);
		return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}

	@ExceptionHandler({ WishlistException.class })
	public ResponseEntity<Object> customHandleException(WishlistException ex, WebRequest request) {
		String userMessage = ex.getMessage();
		String developerMessage = "An error occurred in the system: " + ExceptionUtils.getRootCauseMessage(ex);
		Error error = new Error(userMessage, developerMessage);
		return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY, request);
	}

	@ExceptionHandler
	public ResponseEntity<Object> customHandleException(Exception ex, WebRequest request) {
		String mensagemError = ex.getMessage();
		super.logger.error(mensagemError, ex);
		Error error = new Error("Unexpected error!" + mensagemError, "Unmapped/unknown error: " + mensagemError);
		return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
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
