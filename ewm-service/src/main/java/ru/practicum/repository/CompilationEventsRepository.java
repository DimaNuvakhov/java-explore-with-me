package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.CompilationEvents;

import java.util.List;

public interface CompilationEventsRepository extends JpaRepository<CompilationEvents, Integer> {

    @Query(value = "SELECT event_id FROM compilations_events WHERE compilation_id = ?1", nativeQuery = true)
    List<Integer> findEventIdsWhereCompilationIdIs(Integer compilationId);

}
