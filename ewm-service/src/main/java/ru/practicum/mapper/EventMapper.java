package ru.practicum.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.model.Event;
import ru.practicum.model.State;
import ru.practicum.model.dto.AdminUpdateEventRequest;
import ru.practicum.model.dto.EventFullDto;
import ru.practicum.model.dto.EventShortDto;
import ru.practicum.model.dto.NewEventDto;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventMapper {

    public static Event toEventFromNewEventDto(NewEventDto newEventDto) {
        return new Event(null, newEventDto.getAnnotation(), null, LocalDateTime.now(),
                newEventDto.getDescription(), newEventDto.getEventDate(), null,
                null, newEventDto.getPaid(),
                newEventDto.getParticipantLimit(), null, newEventDto.getRequestModeration(),
                State.PENDING.toString(), newEventDto.getTitle()
        );
    }

    public static Event toEventFromAdminUpdateEventRequest(AdminUpdateEventRequest adminUpdateEventRequest) {
        return new Event(null, adminUpdateEventRequest.getAnnotation(), null, null,
                adminUpdateEventRequest.getDescription(), adminUpdateEventRequest.getEventDate(), null,
                LocationMapper.toLocation(adminUpdateEventRequest.getLocation()), adminUpdateEventRequest.getPaid(),
                adminUpdateEventRequest.getParticipantLimit(), null, adminUpdateEventRequest.getRequestModeration(),
                null, adminUpdateEventRequest.getTitle()
        );
    }

    public static EventFullDto toEventFullDto(Event event) {
        return new EventFullDto(event.getId(), event.getAnnotation(),
                CategoryMapper.toCategoryDto(event.getCategory()), null, event.getCreatedOn(),
                event.getDescription(), event.getEventDate(), UserMapper.toUserShortDto(event.getInitiator()),
                LocationMapper.toLocationDto(event.getLocation()), event.getPaid(), event.getParticipantLimit(),
                event.getPublishedOn(), event.getRequestModeration(), event.getState(), event.getTitle(),
                null
        );
    }

    public static EventShortDto toEventShortDto(Event event) {
        return new EventShortDto(event.getId(), event.getAnnotation(),
                CategoryMapper.toCategoryDto(event.getCategory()),
                null, event.getEventDate(), UserMapper.toUserShortDto(event.getInitiator()),
                event.getPaid(), event.getTitle(), null
        );
    }
}
