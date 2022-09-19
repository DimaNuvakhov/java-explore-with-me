package ru.practicum.service.interfaces;

import ru.practicum.model.dto.CompilationDto;
import ru.practicum.model.dto.NewCompilationDto;

public interface CompilationService {

    CompilationDto post(NewCompilationDto newCompilationDto);

}
