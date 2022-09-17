package ru.practicum.model.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.model.State;

import java.time.LocalDateTime;

@Getter
@Setter
public class EventFullDto {

    private Integer id;

    private String annotation;

    private CategoryDto category;

    private Integer confirmedRequests;

    private LocalDateTime createdOn;

    private String description;

    private LocalDateTime eventDate;

    private UserShortDto initiator;

    private LocationDto location;

    private Boolean paid;

    private Integer participantLimit;

    private LocalDateTime publishedOn;

    private Boolean requestModeration;

    private State state;

    private String title;

    private Integer views;

    public EventFullDto(Integer id, String annotation, CategoryDto category, Integer confirmedRequests,
                        LocalDateTime createdOn, String description, LocalDateTime eventDate, UserShortDto initiator,
                        LocationDto location, Boolean paid, Integer participantLimit, LocalDateTime publishedOn,
                        Boolean requestModeration, State state, String title, Integer views) {
        this.id = id;
        this.annotation = annotation;
        this.category = category;
        this.confirmedRequests = confirmedRequests;
        this.createdOn = createdOn;
        this.description = description;
        this.eventDate = eventDate;
        this.initiator = initiator;
        this.location = location;
        this.paid = paid;
        this.participantLimit = participantLimit;
        this.publishedOn = publishedOn;
        this.requestModeration = requestModeration;
        this.state = state;
        this.title = title;
        this.views = views;
    }
}
