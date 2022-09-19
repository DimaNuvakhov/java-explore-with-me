package ru.practicum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.client.EventClient;
import ru.practicum.commonLibrary.Library;
import ru.practicum.exception.EventNotFoundException;
import ru.practicum.mapper.CompilationMapper;
import ru.practicum.mapper.EventMapper;
import ru.practicum.model.CompilationEvents;
import ru.practicum.model.Status;
import ru.practicum.model.dto.CompilationDto;
import ru.practicum.model.dto.EventShortDto;
import ru.practicum.model.dto.NewCompilationDto;
import ru.practicum.repository.CompilationEventsRepository;
import ru.practicum.repository.CompilationRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.ParticipationRequestRepository;
import ru.practicum.service.interfaces.CompilationService;

import java.util.ArrayList;
import java.util.List;

@Service
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;

    private final CompilationEventsRepository compilationEventsRepository;

    private final EventRepository eventRepository;

    private final EventClient eventClient;

    private final ParticipationRequestRepository requestRepository;

    @Autowired
    public CompilationServiceImpl(CompilationRepository compilationRepository, CompilationEventsRepository compilationEventsRepository, EventRepository eventRepository, EventClient eventClient, ParticipationRequestRepository requestRepository) {
        this.compilationRepository = compilationRepository;
        this.compilationEventsRepository = compilationEventsRepository;
        this.eventRepository = eventRepository;
        this.eventClient = eventClient;
        this.requestRepository = requestRepository;
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
        List<EventShortDto> events = new ArrayList<>();
        for (Integer eventId : compilationEventsRepository.findEventIdsWhereCompilationIdIs(compilationDto.getId())) {
            EventShortDto eventShortDto = EventMapper.toEventShortDto(eventRepository.findById(eventId)
                    .orElseThrow(() -> new EventNotFoundException("Event with id " + eventId + " was not found.")));
            eventShortDto.setConfirmedRequests(requestRepository.findAllByEventAndStatusIs(
                    eventId, Status.APPROVED.toString()).size());
            String uri = "/events/" + eventShortDto.getId();
            eventShortDto.setViews(Library.getViews(uri, eventRepository, eventClient));
            events.add(eventShortDto);
        }
        compilationDto.setEvents(events);
        return compilationDto;
    }

    public void deleteCompilationById(Integer compId) {
        compilationRepository.deleteById(compId);
    }
}
