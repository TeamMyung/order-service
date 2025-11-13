package com.sparta.orderservice.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.sparta.orderservice.global.dto.ApiResponse;

import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<ApiResponse<?>> handleNotFound(EntityNotFoundException e) {
		return ResponseEntity
			.status(ErrorCode.PRODUCT_NOT_FOUND.getStatus().value())
			.body(new ApiResponse<>(ErrorCode.PRODUCT_NOT_FOUND));
	}

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ApiResponse<?>> handleCustomException(CustomException e) {
		ErrorCode code = e.getErrorCode();
		return ResponseEntity
			.status(code.getStatus())
			.body(new ApiResponse<>(code));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse<?>> handleGeneral(Exception e) {
		return ResponseEntity
			.status(ErrorCode.INTERNAL_SERVER_ERROR.getStatus().value())
			.body(new ApiResponse<>(ErrorCode.INTERNAL_SERVER_ERROR));
	}
}
