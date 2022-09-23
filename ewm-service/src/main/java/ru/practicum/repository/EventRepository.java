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

    @Query(value = "SELECT MAX(event_date) from events", nativeQuery = true)
    LocalDateTime findMaxEventDate();

    Page<Event> findAllByInitiatorId(Integer initiatorId, Pageable pageable);

    @Query(value = "select * from events where event_date between ?1 and ?2 and initiator_id in ?3 and state in ?4" +
            " and category_id in ?5 order by event_date", nativeQuery = true)
    Page<Event> getEvents(LocalDateTime rangeStart, LocalDateTime rangeEnd, List<Integer> users, List<String> states,
                          List<Integer> categories, Pageable pageable);

    @Query(value = "select * from (select * from events where upper(annotation) like upper(concat('%', ?1, '%')) " +
            "or upper(description) like upper(concat('%', ?1, '%'))) as e where category_id in (?2) and paid = ?3 " +
            "and event_date between ?4 and ?5 and state = 'PUBLISHED'", nativeQuery = true)
    List<Event> getPublicEvents(String text, List<Integer> categories, Boolean paid, LocalDateTime rangeStart,
                                LocalDateTime rangeEnd, Pageable pageable);

    @Query(value = "select * from (select * from events where upper(annotation) like upper(concat('%', ?1, '%')) " +
            "or upper(description) like upper(concat('%', ?1, '%'))) as e where category_id in (?2) and paid = ?3 " +
            "and event_date > now()  and state = 'PUBLISHED'", nativeQuery = true)
    List<Event> getPublicEventsWithoutDates(String text, List<Integer> categories, Boolean paid,
                                            Pageable pageable);

    Boolean existsByCategoryId(Integer catId);
}
