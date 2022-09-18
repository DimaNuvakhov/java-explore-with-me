package ru.practicum.service.interfaces;

import ru.practicum.model.dto.EventFullDto;
import ru.practicum.model.dto.NewEventDto;

public interface EventService {

    EventFullDto post(NewEventDto newEventDto, Integer userId);

    EventFullDto cancelEvent(Integer userId, Integer eventId);

    EventFullDto getEventByIdPublic(Integer eventId, String ip, String uri);

    EventFullDto publishEvent(Integer eventId);

}
