package ru.practicum.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryDto {

    private Integer id;

    private String name;

    public CategoryDto(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
