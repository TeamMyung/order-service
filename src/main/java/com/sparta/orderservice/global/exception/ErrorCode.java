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
	// 공통
	INVALID_REQUEST_PARAMETER(5001, HttpStatus.BAD_REQUEST, "잘못된 요청 파라미터입니다."),
	INTERNAL_SERVER_ERROR(5002, HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."),
	DATABASE_ERROR(5003, HttpStatus.INTERNAL_SERVER_ERROR, "데이터베이스 처리 중 오류가 발생했습니다."),
	// Product
	PRODUCT_NOT_FOUND(5100, HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다."),
	INVALID_PRODUCT_STATUS(5101, HttpStatus.BAD_REQUEST, "유효하지 않은 상품 상태입니다. (허용: PENDING, APPROVED, DENIED)"),
	PRODUCT_ALREADY_APPROVED(5102, HttpStatus.BAD_REQUEST, "이미 승인된 상품입니다."),
	PRODUCT_ALREADY_DENIED(5103, HttpStatus.BAD_REQUEST, "이미 거절된 상품입니다."),
	PRODUCT_CREATION_FAILED(5104, HttpStatus.INTERNAL_SERVER_ERROR, "상품 등록 중 오류가 발생했습니다."),
	PRODUCT_UPDATE_FAILED(5105, HttpStatus.INTERNAL_SERVER_ERROR, "상품 수정 중 오류가 발생했습니다."),
	PRODUCT_LIST_EMPTY(5106, HttpStatus.NOT_FOUND, "조건에 맞는 상품이 존재하지 않습니다."),
	PRODUCT_ACCESS_DENIED(5107, HttpStatus.FORBIDDEN, "상품에 대한 접근 권한이 없습니다."),
	PRODUCT_ALREADY_DELETED(5108, HttpStatus.BAD_REQUEST, "이미 삭제된 상품입니다."),
	PRODUCT_DELETE_FORBIDDEN(5109, HttpStatus.FORBIDDEN, "해당 상품에 대한 삭제 권한이 없습니다."),
	PRODUCT_DELETE_FAILED(5110, HttpStatus.INTERNAL_SERVER_ERROR, "상품 삭제 중 오류가 발생했습니다."),
	// Hub
	HUB_NOT_FOUND(5200, HttpStatus.NOT_FOUND, "허브를 찾을 수 없습니다."),
	HUB_UNAUTHORIZED(5201, HttpStatus.FORBIDDEN, "허브 접근 권한이 없습니다."),
	// Vendor
	VENDOR_NOT_FOUND(5300, HttpStatus.NOT_FOUND, "업체를 찾을 수 없습니다."),
	VENDOR_UNAUTHORIZED(5301, HttpStatus.FORBIDDEN, "업체 접근 권한이 없습니다."),

	// 주문 에러 코드 : 6000번대
	PRODUCT_SERVICE_UNAVAILABLE(6001, HttpStatus.SERVICE_UNAVAILABLE, "상품 서비스와의 통신에 실패했습니다."),  // FeignException.ServiceUnavailable
	PRODUCT_RESPONSE_MAPPING_FAILED(6002, HttpStatus.BAD_GATEWAY, "상품 서비스 응답 매핑에 실패했습니다."),     // FeignException 발생 시 JSON 변환 실패
	PRODUCT_RESPONSE_INVALID_FORMAT(6003, HttpStatus.BAD_REQUEST, "상품 서비스 응답 형식이 올바르지 않습니다."), // 구조 불일치나 null 응답

	PRODUCT_NOT_FOUND_ORDER(6004, HttpStatus.NOT_FOUND, "주문 대상 상품을 찾을 수 없습니다."), // getProductById() 결과 null
	INSUFFICIENT_STOCK(6005, HttpStatus.BAD_REQUEST, "주문 수량이 재고를 초과했습니다."),        // 수량 초과

	PRODUCT_STOCK_UPDATE_FAILED(6006, HttpStatus.INTERNAL_SERVER_ERROR, "상품 재고 차감 중 오류가 발생했습니다."), // Feign PUT 실패

	ORDER_CREATION_FAILED(6007, HttpStatus.INTERNAL_SERVER_ERROR, "주문 생성 중 오류가 발생했습니다."), // DB save 실패
	ORDER_NOT_FOUND(6008, HttpStatus.NOT_FOUND, "주문을 찾을 수 없습니다."), // 이후 조회/취소 로직에서 사용 예정
	ORDER_ACCESS_DENIED(6009, HttpStatus.FORBIDDEN, "해당 주문에 대한 접근 권한이 없습니다."),
	ORDER_STATUS_INVALID(6010, HttpStatus.BAD_REQUEST, "유효하지 않은 주문 상태입니다."),
	ORDER_CANCELLATION_FAILED(6011, HttpStatus.INTERNAL_SERVER_ERROR, "주문 취소 처리 중 오류가 발생했습니다."

	);

	private final int code;
	private final HttpStatus status;
	private final String details;

	ErrorCode(int code, String details, HttpStatus status) {
		this.code = code;
		this.details = details;
		this.status = status;
	}
}

