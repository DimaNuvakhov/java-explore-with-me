package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.client.EventClient;
import ru.practicum.commonLibrary.Library;
import ru.practicum.exception.CompilationEventsNotFoundException;
import ru.practicum.exception.CompilationNotFoundException;
import ru.practicum.exception.EventNotFoundException;
import ru.practicum.exception.InvalidAccessException;
import ru.practicum.mapper.CompilationMapper;
import ru.practicum.mapper.EventMapper;
import ru.practicum.model.Compilation;
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
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;

    private final CompilationEventsRepository compilationEventsRepository;

    private final EventRepository eventRepository;

    private final EventClient eventClient;

    private final ParticipationRequestRepository requestRepository;

    public CompilationDto post(NewCompilationDto newCompilationDto) {
        CompilationDto compilationDto = CompilationMapper.toCompilationDto(compilationRepository
                .save(CompilationMapper.toCompilation(newCompilationDto)));
        for (Integer eventId : newCompilationDto.getEvents()) {
            if (!eventRepository.existsById(eventId)) {
                throw new EventNotFoundException("Event with id " + eventId + " was not found.");
            }
            compilationEventsRepository.save(new CompilationEvents(null, compilationDto.getId(), eventId));
        }
        compilationDto.setEvents(findCompilationEventsAndSetConfirmedRequests(compilationDto));
        return compilationDto;
    }

    public void deleteCompilationById(Integer compId) {
        if (!compilationRepository.existsById(compId)) {
            throw new CompilationNotFoundException("Compilation with id " + compId + " was not found.");
        }
        compilationRepository.deleteById(compId);
    }

    public void deleteEventFromCompilation(Integer compId, Integer eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new EventNotFoundException("Event with id " + eventId + " was not found.");
        }
        if (!compilationRepository.existsById(compId)) {
            throw new CompilationNotFoundException("Compilation with id " + compId + " was not found.");
        }
        if (!compilationEventsRepository.existsByCompilationIdAndEventId(compId, eventId)) {
            throw new CompilationEventsNotFoundException(
                    "Compilation with id " + compId + " does not contain an event with id " + eventId + ".");
        }
        CompilationEvents compilationEvent = compilationEventsRepository.findByCompilationIdAndEventId(compId, eventId);
        compilationEventsRepository.deleteById(compilationEvent.getId());
    }

    public void addEventToCompilation(Integer compId, Integer eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new EventNotFoundException("Event with id " + eventId + " was not found.");
        }
        if (!compilationRepository.existsById(compId)) {
            throw new CompilationNotFoundException("Compilation with id " + compId + " was not found.");
        }
        if (compilationEventsRepository.existsByCompilationIdAndEventId(compId, eventId)) {
            throw new CompilationEventsNotFoundException(
                    "Compilation with id " + compId + " has already contained an event with id " + eventId + ".");
        }
        compilationEventsRepository.save(new CompilationEvents(null, compId, eventId));
    }

    public void pinCompilation(Integer compId) {
        Compilation foundedCompilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new CompilationNotFoundException("Compilation with id " + compId + " was not found."));
        if (foundedCompilation.getPinned()) {
            throw new InvalidAccessException("Compilation already pinned");
        }
        foundedCompilation.setPinned(true);
        compilationRepository.save(foundedCompilation);
    }

    public void unpinCompilation(Integer compId) {
        Compilation foundedCompilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new CompilationNotFoundException("Compilation with id " + compId + " was not found."));
        if (!foundedCompilation.getPinned()) {
            throw new InvalidAccessException("Compilation already unpinned");
        }
        foundedCompilation.setPinned(false);
        compilationRepository.save(foundedCompilation);
    }

    public CompilationDto getCompilationById(Integer compId) {
        CompilationDto compilationDto = CompilationMapper.toCompilationDto(compilationRepository.findById(compId)
                .orElseThrow(() -> new CompilationNotFoundException("Compilation with id " + compId + " was not found.")));
        compilationDto.setEvents(findCompilationEventsAndSetConfirmedRequests(compilationDto));
        return compilationDto;
    }

    public List<CompilationDto> getAllCompilations(Boolean pinned, Integer from, Integer size) {
        PageRequest pageRequest = PageRequest.of(from / size, size, Sort.by("id"));
        List<CompilationDto> compilationDtoList;
        if (pinned) {
            compilationDtoList = compilationRepository.findAllWhenPinnedIs(true, pageRequest).stream()
                    .map(CompilationMapper::toCompilationDto).collect(Collectors.toList());
        } else {
            compilationDtoList = compilationRepository.findAllWhenPinnedIs(false, pageRequest).stream()
                    .map(CompilationMapper::toCompilationDto).collect(Collectors.toList());
        }
        for (CompilationDto compilationDto : compilationDtoList) {
            compilationDto.setEvents(findCompilationEventsAndSetConfirmedRequests(compilationDto));
        }
        return compilationDtoList;
    }

    private List<EventShortDto> findCompilationEventsAndSetConfirmedRequests(CompilationDto compilationDto) {
        List<EventShortDto> events = new ArrayList<>();
        for (Integer eventId : compilationEventsRepository.findEventIdsWhereCompilationIdIs(compilationDto.getId())) {
            EventShortDto eventShortDto = EventMapper.toEventShortDto(eventRepository.findById(eventId)
                    .orElseThrow(() -> new EventNotFoundException("Event with id " + eventId + " was not found.")));
            eventShortDto.setConfirmedRequests(requestRepository.findAllByEventAndStatusIs(
                    eventId, Status.CONFIRMED.toString()).size());
            String uri = "/events/" + eventShortDto.getId();
            eventShortDto.setViews(Library.getViews(uri, eventRepository, eventClient));
            events.add(eventShortDto);
        }
        return events;
    }
}
