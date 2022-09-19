package ru.practicum.service.interfaces;

import ru.practicum.model.dto.CompilationDto;
import ru.practicum.model.dto.NewCompilationDto;

import java.util.List;

public interface CompilationService {

    CompilationDto getCompilationById(Integer compId);

    List<CompilationDto> getAllCompilations(Boolean pinned, Integer from, Integer size);

    CompilationDto post(NewCompilationDto newCompilationDto);

    void deleteCompilationById(Integer compId);

    void deleteEventFromCompilation(Integer compId, Integer eventId);

    void addEventToCompilation(Integer compId, Integer eventId);

    void pinCompilation(Integer compId);

    void unpinCompilation(Integer compId);
}
