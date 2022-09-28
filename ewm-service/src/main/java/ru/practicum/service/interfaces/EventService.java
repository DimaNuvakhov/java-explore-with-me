package ru.practicum.service.interfaces;

import ru.practicum.model.dto.*;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    EventFullDto post(NewEventDto newEventDto, Integer userId);

    EventFullDto cancelEvent(Integer userId, Integer eventId);

    EventFullDto getEventByIdPublic(Integer eventId, String ip, String uri);

    EventFullDto publishEvent(Integer eventId);

    EventFullDto rejectEvent(Integer eventId);

    List<EventShortDto> getAllUsersEvents(Integer userId, Integer from, Integer size);

    EventFullDto getUserEvent(Integer userId, Integer eventId);

    EventFullDto updateEventByUser(Integer userId, UpdateEventRequest updateEventRequest);

    EventFullDto putEventByAdmin(Integer eventId, AdminUpdateEventRequest adminUpdateEventRequest);

    List<EventFullDto> getEventsByAdmin(List<Integer> users, List<String> states, List<Integer> categories,
                                        LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from,
                                        Integer size);

    List<EventFullDto> getPublicEvents(String text, List<Integer> categories, Boolean paid, LocalDateTime rangeStart,
                    LocalDateTime rangeEnd, Boolean onlyAvailable, String sort,
                    Integer from, Integer size, String ip, String uri);
}
