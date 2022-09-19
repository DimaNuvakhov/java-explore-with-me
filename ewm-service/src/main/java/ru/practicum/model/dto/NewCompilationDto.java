package ru.practicum.model.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class NewCompilationDto {

    @NotNull
    private List<Integer> events;

    @NotNull
    private Boolean pinned;

    @NotEmpty
    private String title;

    public NewCompilationDto(List<Integer> events, Boolean pinned, String title) {
        this.events = events;
        this.pinned = pinned;
        this.title = title;
    }
}
