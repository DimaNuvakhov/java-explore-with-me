package ru.practicum.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserShortDto {

    private Integer id;

    private String name;

    public UserShortDto(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
