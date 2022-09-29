package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.dto.CategoryDto;
import ru.practicum.service.interfaces.CategoryService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping()
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/categories")
    public Collection<CategoryDto> getAll(@PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                          @Positive @RequestParam(defaultValue = "10") Integer size) {
        return categoryService.getAll(from, size);
    }

    @GetMapping("/categories/{catId}")
    public CategoryDto getById(@PathVariable Integer catId) {
        return categoryService.getById(catId);
    }

    @PatchMapping("/admin/categories")
    public CategoryDto patch(@RequestBody CategoryDto categoryDto) {
        return categoryService.patch(categoryDto);
    }

    @PostMapping("/admin/categories")
    public CategoryDto post(@RequestBody @Valid CategoryDto categoryDto) {
        return categoryService.post(categoryDto);
    }

    @DeleteMapping("/admin/categories/{catId}")
    public void deleteById(@PathVariable Integer catId) {
        categoryService.deleteById(catId);
    }
}
