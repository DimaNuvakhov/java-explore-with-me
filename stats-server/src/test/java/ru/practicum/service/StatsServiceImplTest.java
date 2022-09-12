package ru.practicum.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.practicum.model.EndpointHit;
import ru.practicum.repository.StatsRepository;

import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

class StatsServiceImplTest {

    private StatsService statsService;
    private StatsRepository statsRepository = Mockito.mock(StatsRepository.class);

    private EndpointHit endpointHit = new EndpointHit(1, "ewm-service",
            "http://localhost:8080/events/2", "192.168.123.132.", LocalDateTime.now());

    @BeforeEach
    public void beforeEach() {
        statsService = new StatsServiceImpl(statsRepository);
    }

    @Test
    void add() {
        Mockito
                .when(statsRepository.save(Mockito.any(EndpointHit.class)))
                .thenReturn(endpointHit);
        EndpointHit foundedEndpointHit = statsService.add(endpointHit);
        assertThat(foundedEndpointHit.getId(), equalTo(endpointHit.getId()));
    }

    @Test
    void get() {

    }
}