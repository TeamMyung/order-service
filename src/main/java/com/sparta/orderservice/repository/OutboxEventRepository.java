package com.sparta.orderservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.orderservice.entity.OutboxEventEntity;

import com.sparta.orderservice.entity.OutboxEventEntity.Status;

public interface OutboxEventRepository extends JpaRepository<OutboxEventEntity, Long> {
	List<OutboxEventEntity> findTop50ByStatusOrderByCreatedAtAsc(Status status);
}

