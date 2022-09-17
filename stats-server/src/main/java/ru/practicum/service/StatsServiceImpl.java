package ru.practicum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStats;
import ru.practicum.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;

    @Autowired
    public StatsServiceImpl(StatsRepository statsRepository) {
        this.statsRepository = statsRepository;
    }

    public EndpointHit add(EndpointHit endpointHit) {
//        endpointHit.setTimestamp(LocalDateTime.now());
        return statsRepository.save(endpointHit);
    }

    public List<ViewStats> get(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        List<ViewStats> stats = new ArrayList<>();
        // Тут я полагаю, что помимо нашего основного сервиса возможны и другие
        for (String app : statsRepository.findApp()) {
            for (String uri : uris) {
                if (unique && statsRepository.statsWithUniqueIp(start, end, uri, app) != 0) {
                    stats.add(new ViewStats(app, uri, statsRepository.statsWithUniqueIp(start, end, uri, app)));
                } else if (!unique && statsRepository.statsWithoutUniqueIp(start, end, uri, app) != 0) {
                    stats.add(new ViewStats(app, uri, statsRepository.statsWithoutUniqueIp(start, end, uri, app)));
                }
            }
        }
        return stats;
    }
}
