package ru.practicum.model.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class CategoryDto {

    private Integer id;

    @NotEmpty
    private String name;

    public CategoryDto(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
