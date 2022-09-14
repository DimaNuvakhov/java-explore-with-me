package ru.practicum.model.mapper;

import ru.practicum.model.Category;
import ru.practicum.model.dto.CategoryDto;

import java.util.ArrayList;
import java.util.List;

public class CategoryMapper {

    public static CategoryDto toCategoryDto(Category category) {
        return new CategoryDto(category.getId(), category.getName());
    }

    public static Category toCategory(CategoryDto categoryDto) {
        return new Category(categoryDto.getId(), categoryDto.getName());
    }

    public static List<CategoryDto> toCategoryDtoList(List<Category> categories) {
        List<CategoryDto> categoryDtos = new ArrayList<>();
        for (Category category : categories) {
            categoryDtos.add(toCategoryDto(category));
        }
        return categoryDtos;
    }
}

