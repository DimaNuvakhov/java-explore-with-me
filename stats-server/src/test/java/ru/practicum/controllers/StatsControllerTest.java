package ru.practicum.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.model.ViewStats;
import ru.practicum.service.StatsService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.hamcrest.Matchers.hasSize;

import static org.mockito.Mockito.when;

import java.util.List;

@WebMvcTest(controllers = StatsController.class)
class StatsControllerTest {

    @Autowired
    ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    StatsService statsService;

    List<ViewStats> viewStatsList = List.of(
            new ViewStats("ewm-service", "http://localhost:8080/events/2", 3),
            new ViewStats("ewm-service", "http://localhost:8080/events/1", 4)
    );

    private String[] uris = {"http://localhost:8080/events/2", "http://localhost:8080/events/1"};

    @Test
    void getViewStats() throws Exception {
        when(statsService.get(LocalDateTime.of(
                        2022, 9, 10, 18, 31, 10),
                LocalDateTime.of(2022, 9, 17, 22, 31, 10),
                uris, false))
                .thenReturn(viewStatsList);
        mvc.perform(get("/stats?start=2022-09-10T18:31:10&end=2022-09-17T22:31:10&uris=" +
                        "http://localhost:8080/events/2&uris=http://localhost:8080/events/1&unique=false")
                        .content(mapper.writeValueAsString(viewStatsList))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].app", containsInAnyOrder("ewm-service", "ewm-service")))
                .andExpect(jsonPath("$[*].uri", containsInAnyOrder("http://localhost:8080/events/2",
                        "http://localhost:8080/events/1")))
                .andExpect(jsonPath("$[*].hits", containsInAnyOrder(3,4)));

    }
}