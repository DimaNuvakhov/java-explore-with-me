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

    @Query(value = "select * from events where event_date between ?1 and ?2 and initiator_id in ?3 and state in ?4" +
            " and category_id in ?5 order by event_date", nativeQuery = true)
    Page<Event> getEvents(LocalDateTime rangeStart, LocalDateTime rangeEnd, List<Integer> users, List<String> states,
                          List<Integer> categories, Pageable pageable);

}
