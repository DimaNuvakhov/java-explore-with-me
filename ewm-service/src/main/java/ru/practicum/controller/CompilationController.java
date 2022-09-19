package ru.practicum.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.dto.CompilationDto;
import ru.practicum.model.dto.NewCompilationDto;
import ru.practicum.service.interfaces.CompilationService;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping()
public class CompilationController {

    private final CompilationService compilationService;

    @Autowired
    public CompilationController(CompilationService compilationService) {
        this.compilationService = compilationService;
    }

    // Admin

    @PostMapping("/admin/compilations")
    public CompilationDto post(@RequestBody @Valid NewCompilationDto newCompilationDto) {
        return compilationService.post(newCompilationDto);
    }

    @DeleteMapping("/admin/compilations/{compId}")
    public void deleteCompilationById(@PathVariable Integer compId) {
        compilationService.deleteCompilationById(compId);
    }

}
