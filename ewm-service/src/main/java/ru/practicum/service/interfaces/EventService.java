package ru.practicum.service.interfaces;

import ru.practicum.model.dto.*;

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
}
