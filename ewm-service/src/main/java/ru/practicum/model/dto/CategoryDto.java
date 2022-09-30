package ru.practicum.model.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class CategoryDto {

    private Integer id;

    @NotBlank
    @NotEmpty
    private String name;

    public CategoryDto(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
