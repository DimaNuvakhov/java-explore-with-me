package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.Event;

import java.time.LocalDateTime;

public interface EventRepository extends JpaRepository<Event, Integer> {

    @Query(value = "SELECT MIN(published_on) from events", nativeQuery = true)
    LocalDateTime findMinPublishedOn();

    @Query(value = "SELECT MAX(published_on) from events", nativeQuery = true)
    LocalDateTime findMaxPublishedOn();


}
