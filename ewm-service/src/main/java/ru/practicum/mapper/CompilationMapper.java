package ru.practicum.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.model.Compilation;
import ru.practicum.model.dto.CompilationDto;
import ru.practicum.model.dto.NewCompilationDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CompilationMapper {

    public static CompilationDto toCompilationDto(Compilation compilation) {
        return new CompilationDto(compilation.getId(), null, compilation.getPinned(),
                compilation.getTitle());
    }

    public static Compilation toCompilation(NewCompilationDto newCompilationDto) {
        return new Compilation(null, newCompilationDto.getPinned(), newCompilationDto.getTitle());
    }
}
