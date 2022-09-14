package ru.practicum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.model.Category;
import ru.practicum.model.dto.CategoryDto;
import ru.practicum.model.mapper.CategoryMapper;
import ru.practicum.repository.CategoryRepository;

import java.util.List;

@Service
public class CategoryServiceImpl {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryDto post(CategoryDto categoryDto) {
        return CategoryMapper.toCategoryDto(categoryRepository.save(CategoryMapper.toCategory(categoryDto)));
    }

    public List<CategoryDto> getAll(Integer from, Integer size) {
       return CategoryMapper.toCategoryDtoList(categoryRepository.findAll());
    }

    public void deleteById(Integer catId) {
        categoryRepository.deleteById(catId);
    }

    public CategoryDto getById(Integer catId) {
        Category category = categoryRepository.findById(catId).orElse(new Category()); // TODO тут надо будет выбросить исключение
        return CategoryMapper.toCategoryDto(category);
    }



}
