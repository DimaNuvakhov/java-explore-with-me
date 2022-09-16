package ru.practicum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.model.Category;
import ru.practicum.model.dto.CategoryDto;
import ru.practicum.mappers.CategoryMapper;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.service.interfaces.CategoryService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CategoryDto post(CategoryDto categoryDto) {
        return CategoryMapper.toCategoryDto(categoryRepository.save(CategoryMapper.toCategory(categoryDto)));

    }
    @Override
    public List<CategoryDto> getAll(Integer from, Integer size) {
        PageRequest pageRequest = PageRequest.of(from / size, size);
        return categoryRepository.findAll(pageRequest).stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }
    @Override
    public void deleteById(Integer catId) {
        categoryRepository.deleteById(catId);
    }

    @Override
    public CategoryDto getById(Integer catId) {
        Category category = categoryRepository.findById(catId).orElse(new Category()); // TODO тут надо будет выбросить исключение
        return CategoryMapper.toCategoryDto(category);
    }

    @Override
    public CategoryDto patch(CategoryDto categoryDto) {
        Category updatedCategory = categoryRepository.findById(categoryDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Ошибка")); // TODO тут надо будет выбросить исключение
        if (categoryDto.getName() != null) {
            updatedCategory.setName(updatedCategory.getName());
        }
        return CategoryMapper.toCategoryDto(categoryRepository.save(updatedCategory));
    }
}
