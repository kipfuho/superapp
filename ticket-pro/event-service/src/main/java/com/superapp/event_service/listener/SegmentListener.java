package com.superapp.event_service.listener;

import org.springframework.stereotype.Component;

import com.superapp.event_service.domain.Segment;
import com.superapp.event_service.util.IdGenerator;

import jakarta.persistence.PrePersist;

@Component
public class SegmentListener {
    private final IdGenerator generator;

    public SegmentListener(IdGenerator generator) {
        this.generator = generator;
    }

    @PrePersist
    public void prePersist(Segment segment) {
        if (segment.getId() == null) {
            segment.setId(generator.generateId());
        }
    }
}
