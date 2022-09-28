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
public class NewEventDto {

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
    private LocationDto location;

    @NotNull
    private Boolean paid;

    @PositiveOrZero
    @NotNull
    private Integer participantLimit;

    @NotNull
    private Boolean requestModeration;

    @NotBlank
    @NotEmpty
    private String title;

    public NewEventDto(String annotation, Integer category, String description, LocalDateTime eventDate,
                       LocationDto location, Boolean paid, Integer participantLimit, Boolean requestModeration,
                       String title) {
        this.annotation = annotation;
        this.category = category;
        this.description = description;
        this.eventDate = eventDate;
        this.location = location;
        this.paid = paid;
        this.participantLimit = participantLimit;
        this.requestModeration = requestModeration;
        this.title = title;
    }
}
