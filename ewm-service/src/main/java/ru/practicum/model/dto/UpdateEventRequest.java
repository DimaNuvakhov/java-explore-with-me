package ru.practicum.model.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@Getter
@Setter
public class UpdateEventRequest {

    @NotBlank
    @NotEmpty
    private String annotation;

    @NotNull
    private Integer category;

    @NotBlank
    @NotEmpty
    private String description;

    @NotNull
    private LocalDateTime eventDate;

    @NotNull
    private Integer eventId;

    @NotNull
    private Boolean paid;

    @PositiveOrZero
    @NotNull
    private Integer participantLimit;

    @NotBlank
    @NotEmpty
    private String title;

    public UpdateEventRequest(String annotation, Integer category, String description, LocalDateTime eventDate,
                              Integer eventId, Boolean paid, Integer participantLimit, String title) {
        this.annotation = annotation;
        this.category = category;
        this.description = description;
        this.eventDate = eventDate;
        this.eventId = eventId;
        this.paid = paid;
        this.participantLimit = participantLimit;
        this.title = title;
    }
}
