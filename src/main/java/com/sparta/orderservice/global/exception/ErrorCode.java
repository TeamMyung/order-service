package com.sparta.orderservice.global.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

	// 각자 도메인에 맞게 에러 코드 작성
	USER_NOT_FOUND(1001, HttpStatus.NOT_FOUND, "일치하는 회원 정보를 찾을 수 없습니다."),

	// 상품 에러 코드 : 5000번대
	PRODUCT_NOT_FOUND(5001, "상품을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
	INVALID_REQUEST(5002, "요청이 잘못되었습니다.", HttpStatus.BAD_REQUEST),
	INTERNAL_ERROR(5000, "서버 내부 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR)
	;

	private final int code;
	private final HttpStatus status;
	private final String details;

	ErrorCode(int code, String details, HttpStatus status) {
		this.code = code;
		this.details = details;
		this.status = status;
	}
}

