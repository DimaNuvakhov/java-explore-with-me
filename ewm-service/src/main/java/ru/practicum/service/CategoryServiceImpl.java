package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.exception.CategoryNotFoundException;
import ru.practicum.exception.InvalidAccessException;
import ru.practicum.model.Category;
import ru.practicum.model.dto.CategoryDto;
import ru.practicum.mapper.CategoryMapper;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.service.interfaces.CategoryService;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final EventRepository eventRepository;

    @Override
    public CategoryDto post(CategoryDto categoryDto) {
        return CategoryMapper.toCategoryDto(categoryRepository.save(CategoryMapper.toCategory(categoryDto)));

    }

    @Override
    public List<CategoryDto> getAll(Integer from, Integer size) {
        PageRequest pageRequest = PageRequest.of(from / size, size, Sort.by("id"));
        return categoryRepository.findAll(pageRequest).stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Integer catId) {
        if (!categoryRepository.existsById(catId)) {
            throw new CategoryNotFoundException("Category with id " + catId + " was not found.");
        }
        categoryRepository.deleteById(catId);
    }

    @Override
    public CategoryDto getById(Integer catId) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new CategoryNotFoundException("Category with id " + catId + " was not found."));
        if (eventRepository.existsByCategoryId(catId)) {
            throw new InvalidAccessException("it's forbidden to delete a category if at least one event is" +
                    " associated with it");
        }
        return CategoryMapper.toCategoryDto(category);
    }

    @Override
    public CategoryDto patch(CategoryDto categoryDto) {
        Category updatedCategory = categoryRepository.findById(categoryDto.getId())
                .orElseThrow(() -> new CategoryNotFoundException(
                        "Category with id " + categoryDto.getId() + " was not found."));
        if (categoryDto.getName() != null) {
            updatedCategory.setName(categoryDto.getName());
        }
        return CategoryMapper.toCategoryDto(categoryRepository.save(updatedCategory));
    }
}
