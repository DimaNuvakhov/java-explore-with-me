package ru.practicum.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.model.EndpointHit;
import ru.practicum.service.StatsServiceImpl;

@RestController
@RequestMapping(path = "/hit")
public class EndpointHitController {

    private final StatsServiceImpl statsServiceImpl;

    @Autowired
    public EndpointHitController(StatsServiceImpl statsServiceImpl) {
        this.statsServiceImpl = statsServiceImpl;
    }

    @PostMapping
    public EndpointHit add(@RequestBody EndpointHit endpointHit) {
        return statsServiceImpl.add(endpointHit);
    }
}
