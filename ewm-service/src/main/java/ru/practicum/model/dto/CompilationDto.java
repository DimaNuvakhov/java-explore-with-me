package ru.practicum.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CompilationDto {

    private Integer id;

    private List<EventShortDto> events;

    private Boolean pinned;

    private String title;

    public CompilationDto(Integer id, List<EventShortDto> events, Boolean pinned, String title) {
        this.id = id;
        this.events = events;
        this.pinned = pinned;
        this.title = title;
    }
}
