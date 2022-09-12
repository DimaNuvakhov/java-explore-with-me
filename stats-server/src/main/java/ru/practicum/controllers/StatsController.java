package ru.practicum.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.service.StatsServiceImpl;
import ru.practicum.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/stats")
public class StatsController {

    private final StatsServiceImpl statsServiceImpl;

    @Autowired
    public StatsController(StatsServiceImpl statsServiceImpl) {
        this.statsServiceImpl = statsServiceImpl;
    }

    @GetMapping
    public List<ViewStats> get(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
                               @RequestParam String[] uris,
                               @RequestParam Boolean unique) {
        return statsServiceImpl.get(start, end, uris, unique);
    }
}
