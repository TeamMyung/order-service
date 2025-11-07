package com.sparta.orderservice.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.orderservice.entity.OrderEntity;

public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {
}
