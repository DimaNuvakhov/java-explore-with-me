package ru.practicum.commonLibrary;

import org.springframework.http.ResponseEntity;
import ru.practicum.client.EventClient;
import ru.practicum.repository.EventRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class Library {

    public static Integer getViews(String uri, EventRepository eventRepository, EventClient eventClient) {
        // Получаем минимальную дату
        LocalDateTime start = eventRepository.findMinCreatedOn();
        // Формируем запрос для получения данных с сервиса статистики
        ResponseEntity<Object> list = eventClient.getRequest(start, LocalDateTime.now(), uri, false);
        // Обрабатываем ответ
        List<Map<String, Object>> statsList = (List<Map<String, Object>>) list.getBody();
        Map<String, Object> statsMap = statsList.get(0);
        return (Integer) statsMap.get("hits");
    }
}
