package ru.practicum.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStats;
import ru.practicum.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

class StatsServiceImplTest {

    private StatsService statsService;
    private StatsRepository statsRepository = Mockito.mock(StatsRepository.class);
    private EndpointHit endpointHit = new EndpointHit(1, "ewm-service",
            "http://localhost:8080/events/2", "192.168.123.132.", LocalDateTime.now());
    private List<String> apps = List.of("ewm-service", "secondService");
    private String[] uris = {"http://localhost:8080/events/2"};

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
        assertThat(foundedEndpointHit.getApp(), equalTo(endpointHit.getApp()));
        assertThat(foundedEndpointHit.getUri(), equalTo(endpointHit.getUri()));
        assertThat(foundedEndpointHit.getIp(), equalTo(endpointHit.getIp()));
        assertThat(foundedEndpointHit.getTimestamp(), equalTo(endpointHit.getTimestamp()));
    }

    @Test
    void getWhenUniqueIsTrue() {
        Mockito
                .when(statsRepository.findApp())
                .thenReturn(apps);
        Mockito
                .when(statsRepository.statsWithUniqueIp(LocalDateTime.of(
                        2022,9,10,18,31,10),
                        LocalDateTime.of(2022,9,17,22,31,10),
                        "http://localhost:8080/events/2", "ewm-service"))
                .thenReturn(1);
        List<ViewStats> foundedViewStats = statsService.get(LocalDateTime.of(
                        2022,9,10,18,31,10),
                LocalDateTime.of(2022,9,17,22,31,10),
                uris, true);
        assertThat(foundedViewStats.size(), equalTo(1));
        for (ViewStats viewStats : foundedViewStats) {
            if (viewStats.getApp().equals("http://localhost:8080/events/2") && viewStats.getApp().equals("ewm-service")) {
                assertThat(viewStats.getApp(), equalTo("ewm-service"));
                assertThat(viewStats.getUri(), equalTo("http://localhost:8080/events/2"));
                assertThat(viewStats.getHits(), equalTo(1));
            }
        }
    }

    @Test
    void getWhenUniqueIsFalse() {
        Mockito
                .when(statsRepository.findApp())
                .thenReturn(apps);
        Mockito
                .when(statsRepository.statsWithoutUniqueIp(LocalDateTime.of(
                                2022,9,10,18,31,10),
                        LocalDateTime.of(2022,9,17,22,31,10),
                        "http://localhost:8080/events/2", "ewm-service"))
                .thenReturn(3);
        List<ViewStats> foundedViewStats = statsService.get(LocalDateTime.of(
                        2022,9,10,18,31,10),
                LocalDateTime.of(2022,9,17,22,31,10),
                uris, false);
        assertThat(foundedViewStats.size(), equalTo(1));
        for (ViewStats viewStats : foundedViewStats) {
            if (viewStats.getApp().equals("http://localhost:8080/events/2") && viewStats.getApp().equals("ewm-service")) {
                assertThat(viewStats.getApp(), equalTo("ewm-service"));
                assertThat(viewStats.getUri(), equalTo("http://localhost:8080/events/2"));
                assertThat(viewStats.getHits(), equalTo(3));
            }
        }
    }
}