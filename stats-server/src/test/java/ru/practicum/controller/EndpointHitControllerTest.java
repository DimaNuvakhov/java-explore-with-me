package ru.practicum.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.model.EndpointHit;
import ru.practicum.service.StatsService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = EndpointHitController.class)
class EndpointHitControllerTest {

    @Autowired
    ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    StatsService statsService;

    LocalDateTime date = LocalDateTime.of(2022, 9, 10, 18, 31, 10);
    private EndpointHit endpointHit = new EndpointHit(1, "ewm-service",
            "http://localhost:8080/events/2", "192.168.123.132.", date);


    @Test
    void add() throws Exception {
        when(statsService.add(any()))
                .thenReturn(endpointHit);
        mvc.perform(post("/hit")
                        .content(mapper.writeValueAsString(endpointHit))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(endpointHit.getId()), Integer.class))
                .andExpect(jsonPath("$.app", is(endpointHit.getApp())))
                .andExpect(jsonPath("$.uri", is(endpointHit.getUri())))
                .andExpect(jsonPath("$.ip", is(endpointHit.getIp())));
    }
}