package com.sparta.orderservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.UUID;

import com.sparta.orderservice.entity.enums.OrderStatus;
import com.sparta.orderservice.global.entity.BaseEntity;

@Entity
@Table(name = "p_orders")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "order_id", nullable = false, updatable = false)
	private UUID orderId;

	@Column(name = "producer_id", nullable = false)
	private UUID producerId;

	@Column(name = "receiver_id")
	private UUID receiverId;

	@Column(name = "product_id", nullable = false)
	private UUID productId;

	@Column(name = "delivery_id")
	private UUID deliveryId;

	@Column(nullable = false)
	private Integer quantity;

	@Enumerated(EnumType.STRING)
	@Column(name = "order_status", nullable = false)
	private OrderStatus orderStatus;

	@Column(nullable = true, length = 225)
	private String request;

	private LocalDateTime approvedAt;
	private String approvedBy;

	public void approve(BigInteger userId) {
		this.approvedAt = LocalDateTime.now();
	}

}

