package com.superapp.event_service.repo;

import com.superapp.event_service.domain.Event;
import com.superapp.event_service.domain.EventPerformer;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EventPerformerRepo extends JpaRepository<EventPerformer, UUID> {
    // All headliners for an event (ordered)
    List<EventPerformer> findByEventIdAndHeadlinerTrueOrderByBillingIndex(UUID eventId);

    // Upcoming events for a performer
    @Query("""
              select ep.event from EventPerformer ep
              where ep.performer.id = :performerId
                and ep.event.startDateTime >= :from
              order by ep.event.startDateTime asc
            """)
    List<Event> findUpcomingEventsForPerformer(UUID performerId, Instant from);

    // Festival lineup by stage and time
    List<EventPerformer> findByEventIdOrderByStageNameAscSetStartAsc(UUID eventId);
}