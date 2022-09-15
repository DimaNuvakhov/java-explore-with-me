package ru.practicum.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.dto.CategoryDto;
import ru.practicum.service.interfaces.CategoryService;

import java.util.Collection;

@RestController
@RequestMapping()
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/categories")
    public Collection<CategoryDto> getAll(@RequestParam(defaultValue = "0", required = false) Integer from,
                                          @RequestParam(defaultValue = "10", required = false) Integer size) {
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
    public CategoryDto post(@RequestBody CategoryDto categoryDto) {
        return categoryService.post(categoryDto);
    }

    @DeleteMapping("/admin/categories/{catId}")
    public void deleteById(@PathVariable Integer catId) {
        categoryService.deleteById(catId);
    }
}
