package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<EndpointHit, Integer> {

    @Query(value = "select count(distinct ip) from endpoint_hits" +
            " where timestamp between ?1 and ?2 and uri = ?3 and app = ?4", nativeQuery = true)
    Integer statsWithUniqueIp(LocalDateTime start, LocalDateTime end, String uri, String app);

    @Query(value = "select count(ip) from endpoint_hits " +
            "where timestamp between ?1 and ?2 and uri = ?3 and app = ?4", nativeQuery = true)
    Integer statsWithoutUniqueIp(LocalDateTime start, LocalDateTime end, String uri, String app);

    @Query(value = "select app from endpoint_hits " +
            "group by app", nativeQuery = true)
    List<String> findApp();

}
