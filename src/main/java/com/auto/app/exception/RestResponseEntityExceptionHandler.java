package com.auto.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.auto.app.model.ErrorMessage;

@ControllerAdvice
@ResponseStatus
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(DocumentNotFoundException.class)
	public ResponseEntity<ErrorMessage> docNotFoundException(DocumentNotFoundException exception){
		ErrorMessage message = new ErrorMessage(HttpStatus.EXPECTATION_FAILED, exception.getMessage());
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
	}
	
	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<ErrorMessage> validatationException(ValidationException exception){
		ErrorMessage message = new ErrorMessage(HttpStatus.EXPECTATION_FAILED, exception.getMessage());
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
	}
		
	@ExceptionHandler(MultipartException.class)
	public ResponseEntity<ErrorMessage> filesNotFoundException(DocumentNotFoundException exception){
		ErrorMessage message = new ErrorMessage(HttpStatus.BAD_REQUEST, exception.getMessage());
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
	}
	
}
