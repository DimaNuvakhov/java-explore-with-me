package ru.practicum.service;

import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {

    EndpointHit add(EndpointHit endpointHit);

    List<ViewStats> get(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);

}
