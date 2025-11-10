package com.sparta.orderservice.controller.vendor;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.orderservice.dto.request.CreateOrderRequestDto;
import com.sparta.orderservice.dto.response.OrderResponseDto;
import com.sparta.orderservice.global.dto.ApiResponse;
import com.sparta.orderservice.service.vendor.VendorOrderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/orders/vendor")
@Tag(name = "Vendor 주문 API", description = "업체(VENDOR)용 주문 관련 API")
//@PreAuthorize("hasRole('ROLE_VENDOR')")
public class VendorOrderController {

	private final VendorOrderService vendorOrderService;

	@Operation(summary = "주문 생성 API", description = "업체(Vendor)가 상품을 주문합니다.")
	@PostMapping
	public ResponseEntity<ApiResponse<OrderResponseDto>> createOrder(
		@Valid @RequestBody CreateOrderRequestDto requestDto
		//,@AuthenticationPrincipal UserAuth userAuth
	) {
		//로그인 되면 제거
		UUID fakeVendorId = UUID.fromString("11111111-1111-1111-1111-111111111111");
		OrderResponseDto response = vendorOrderService.createOrder(requestDto, fakeVendorId);
		return ResponseEntity.ok(new ApiResponse<>(response));
	}
}
