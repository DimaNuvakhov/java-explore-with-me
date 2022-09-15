package ru.practicum.service.interfaces;

import ru.practicum.model.dto.CategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto post(CategoryDto categoryDto);

    List<CategoryDto> getAll(Integer from, Integer size);

    void deleteById(Integer catId);

    CategoryDto getById(Integer catId);

    CategoryDto patch(CategoryDto categoryDto);

}
