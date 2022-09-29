package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.dto.CompilationDto;
import ru.practicum.model.dto.NewCompilationDto;
import ru.practicum.service.interfaces.CompilationService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping()
public class CompilationController {

    private final CompilationService compilationService;
    // Public

    @GetMapping("/compilations")
    public Collection<CompilationDto> getAllCompilations(@RequestParam Boolean pinned,
                                                         @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                         @Positive @RequestParam(defaultValue = "10") Integer size) {
        return compilationService.getAllCompilations(pinned, from, size);
    }

    @GetMapping("/compilations/{compId}")
    public CompilationDto getCompilationById(@PathVariable Integer compId) {
        return compilationService.getCompilationById(compId);
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

    @DeleteMapping("/admin/compilations/{compId}/events/{eventId}")
    public void deleteEventFromCompilation(@PathVariable Integer compId, @PathVariable Integer eventId) {
        compilationService.deleteEventFromCompilation(compId, eventId);
    }

    @PatchMapping("/admin/compilations/{compId}/events/{eventId}")
    public void addEventToCompilation(@PathVariable Integer compId, @PathVariable Integer eventId) {
        compilationService.addEventToCompilation(compId, eventId);
    }

    @PatchMapping("/admin/compilations/{compId}/pin")
    public void pinCompilation(@PathVariable Integer compId) {
        compilationService.pinCompilation(compId);
    }

    @DeleteMapping("/admin/compilations/{compId}/pin")
    public void unpinCompilation(@PathVariable Integer compId) {
        compilationService.unpinCompilation(compId);
    }
}
