package com.sparta.orderservice.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.orderservice.entity.OrderEntity;

public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {
	Page<OrderEntity> findByReceiverId(UUID receiverId, Pageable pageable);
	Page<OrderEntity> findByProducerId(UUID producerId, Pageable pageable);
	Page<OrderEntity> findByHubId(UUID hubId, Pageable pageable);
}
