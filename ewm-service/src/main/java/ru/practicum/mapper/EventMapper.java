package ru.practicum.mapper;

import ru.practicum.model.Event;
import ru.practicum.model.State;
import ru.practicum.model.dto.EventFullDto;
import ru.practicum.model.dto.NewEventDto;

import java.time.LocalDateTime;

public class EventMapper {

    public static Event toEventFromNewEventDto(NewEventDto newEventDto) {
        return new Event(null, newEventDto.getAnnotation(), null, LocalDateTime.now(),
                newEventDto.getDescription(), newEventDto.getEventDate(), null,
                null, newEventDto.getPaid(),
                newEventDto.getParticipantLimit(), null, newEventDto.getRequestModeration(),
                State.PENDING.toString(), newEventDto.getTitle()
        );
    }

    public static EventFullDto toEventFullDto(Event event) {
        return new EventFullDto(event.getId(), event.getAnnotation(),
                CategoryMapper.toCategoryDto(event.getCategory()), null, event.getCreatedOn(),
                event.getDescription(), event.getEventDate(), UserMapper.toUserShortDto(event.getInitiator()),
                LocationMapper.locationDto(event.getLocation()), event.getPaid(), event.getParticipantLimit(),
                event.getPublishedOn(), event.getRequestModeration(), event.getState(), event.getTitle(),
                null
        );
    }

}
