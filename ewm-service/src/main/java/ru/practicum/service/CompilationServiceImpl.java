package ru.practicum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.exception.EventNotFoundException;
import ru.practicum.mapper.CompilationMapper;
import ru.practicum.mapper.EventMapper;
import ru.practicum.model.CompilationEvents;
import ru.practicum.model.dto.CompilationDto;
import ru.practicum.model.dto.EventShortDto;
import ru.practicum.model.dto.NewCompilationDto;
import ru.practicum.repository.CompilationEventsRepository;
import ru.practicum.repository.CompilationRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.service.interfaces.CompilationService;

@Service
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;

    private final CompilationEventsRepository compilationEventsRepository;

    private final EventRepository eventRepository;

    @Autowired
    public CompilationServiceImpl(CompilationRepository compilationRepository, CompilationEventsRepository compilationEventsRepository, EventRepository eventRepository) {
        this.compilationRepository = compilationRepository;
        this.compilationEventsRepository = compilationEventsRepository;
        this.eventRepository = eventRepository;
    }


    public CompilationDto post(NewCompilationDto newCompilationDto) {
        CompilationDto compilationDto = CompilationMapper.toCompilationDto(compilationRepository.
                save(CompilationMapper.toCompilation(newCompilationDto)));
        for (Integer eventId : newCompilationDto.getEvents()) {
            if (!eventRepository.existsById(eventId)) {
                throw new EventNotFoundException("Event with id " + eventId + " was not found.");
            }
            compilationEventsRepository.save(new CompilationEvents(null, compilationDto.getId(), eventId));
        }
        for (Integer eventId : compilationEventsRepository.findEventIdsWhereCompilationIdIs(compilationDto.getId())) {
            EventShortDto eventShortDto = EventMapper.toEventShortDto(eventRepository.findById(eventId)
                    .orElseThrow(() -> new EventNotFoundException("Event with id " + eventId + " was not found.")));

        }
        return null;
    }
}
