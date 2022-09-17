package ru.practicum.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.model.EndpointHit;
import ru.practicum.service.StatsService;

@RestController
@RequestMapping(path = "/hit")
public class EndpointHitController {

    private final StatsService statsService;

    @Autowired
    public EndpointHitController(StatsService statsService) {
        this.statsService = statsService;
    }

    @PostMapping
    public EndpointHit add(@RequestBody EndpointHit endpointHit) {
        return statsService.add(endpointHit);
    }
}
