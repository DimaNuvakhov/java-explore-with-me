package ru.practicum.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer> {

    @Query(value = "SELECT MIN(created_on) from events", nativeQuery = true)
    LocalDateTime findMinCreatedOn();

    Page<Event> findAllByInitiatorId(Integer initiatorId, Pageable pageable);

}
