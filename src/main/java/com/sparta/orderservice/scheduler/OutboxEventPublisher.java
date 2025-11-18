package com.sparta.orderservice.scheduler;

import java.util.List;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.sparta.orderservice.entity.OutboxEventEntity;
import com.sparta.orderservice.repository.OutboxEventRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxEventPublisher {

	private final OutboxEventRepository repository;
	private final KafkaTemplate<String, JsonNode> kafkaTemplate;

	@Scheduled(fixedDelay = 200000) // 2초 마다 발행 시도
	public void publishPendingEvents() {

		List<OutboxEventEntity> events =
			repository.findTop50ByStatusOrderByCreatedAtAsc(OutboxEventEntity.Status.PENDING);

		for (OutboxEventEntity event : events) {
			try {
				kafkaTemplate.send("slack-notification", event.getPayload());

				event.setStatus(OutboxEventEntity.Status.SUCCESS);
				repository.save(event);

				log.info("Outbox 이벤트 발행 성공: {}", event.getId());

			} catch (Exception e) {

				event.setStatus(OutboxEventEntity.Status.FAILED);
				repository.save(event);

				log.error("Outbox 이벤트 발행 실패: {}", event.getId(), e);
			}
		}
	}
}

