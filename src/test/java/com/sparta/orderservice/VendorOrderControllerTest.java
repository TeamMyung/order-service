package com.sparta.orderservice;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.orderservice.dto.request.CreateOrderRequestDto;
import com.sparta.orderservice.global.client.ProductClient;
import com.sparta.orderservice.global.client.ProductResponseDto;

/**
 * ✅ VendorOrderController 단위 테스트
 * ProductService (Feign) 대신 Mock 객체 사용
 * OrderService 단독 실행으로 테스트 가능
 */
@SpringBootTest
@AutoConfigureMockMvc
class VendorOrderControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	// ✅ ProductClient는 Feign이므로 MockBean으로 대체
	@MockBean
	private ProductClient productClient;

	@DisplayName("주문 생성 성공 테스트")
	@Test
	void createOrder_success() throws Exception {
		// given
		UUID productId = UUID.randomUUID();

		// 상품 Mock 데이터 구성 (Builder 방식)
		ProductResponseDto mockProduct = ProductResponseDto.builder()
			.productId(productId.toString())
			.productName("상품A")
			.status("APPROVED")
			.message("정상 승인된 상품")
			.stock(100)
			.price(12000)
			.createdAt(LocalDateTime.now())
			.approvedAt(LocalDateTime.now())
			.approvedBy("admin")
			.build();

		// Feign 호출 결과를 Mock 처리
		given(productClient.getProductById(any(UUID.class))).willReturn(mockProduct);

		// 주문 요청 DTO
		CreateOrderRequestDto requestDto = new CreateOrderRequestDto(
			productId, "상품A", 2, "빠른 출고 부탁드립니다."
		);

		// when & then
		mockMvc.perform(post("/v1/vendor/orders")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestDto)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.productName").value("상품A"))
			.andExpect(jsonPath("$.data.orderStatus").value("PENDING"));
	}

	@DisplayName("재고 부족 시 주문 실패 테스트")
	@Test
	void createOrder_fail_due_to_low_stock() throws Exception {
		// given
		UUID productId = UUID.randomUUID();

		// 재고가 부족한 상품 Mock
		ProductResponseDto mockProduct = ProductResponseDto.builder()
			.productId(productId.toString())
			.productName("상품B")
			.status("APPROVED")
			.message("정상 승인된 상품")
			.stock(1)  // 재고 부족
			.price(15000)
			.createdAt(LocalDateTime.now())
			.approvedAt(LocalDateTime.now())
			.approvedBy("admin")
			.build();

		given(productClient.getProductById(any(UUID.class))).willReturn(mockProduct);

		CreateOrderRequestDto requestDto = new CreateOrderRequestDto(
			productId, "상품B", 5, "재고 부족 테스트"
		);

		// when & then
		mockMvc.perform(post("/v1/vendor/orders")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestDto)))
			.andExpect(status().isBadRequest());
	}
}
