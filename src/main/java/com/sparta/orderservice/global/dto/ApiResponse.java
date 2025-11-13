package com.sparta.orderservice.global.dto;

import com.sparta.orderservice.global.exception.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class ApiResponse<T> {

	private int status;
	private String message;
	private T data;
	private ApiError error;

	public ApiResponse(T data) {
		this.status = 200;
		this.message = "OK";
		this.data = data;
		this.error = null;
	}

	public ApiResponse(ErrorCode error) {
		this.status = error.getStatus().value();
		this.message = error.getStatus().name();
		this.data = null;
		this.error = new ApiError(error.getCode(), error.getDetails());
	}

	@Getter
	@AllArgsConstructor
	public static class ApiError {
		private int code;
		private String details;
	}
}