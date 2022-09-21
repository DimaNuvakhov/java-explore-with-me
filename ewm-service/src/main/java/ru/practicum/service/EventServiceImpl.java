package ru.practicum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.client.EventClient;
import ru.practicum.commonLibrary.Library;
import ru.practicum.exception.*;
import ru.practicum.exception.IllegalStateException;
import ru.practicum.mapper.CategoryMapper;
import ru.practicum.mapper.EventMapper;
import ru.practicum.mapper.LocationMapper;
import ru.practicum.model.*;
import ru.practicum.model.dto.*;
import ru.practicum.repository.*;
import ru.practicum.service.interfaces.EventService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService {

    private final CategoryRepository categoryRepository;

    private final UserRepository userRepository;

    private final LocationRepository locationRepository;

    private final EventRepository eventRepository;

    private final EventClient eventClient;

    private final ParticipationRequestRepository requestRepository;

    @Autowired
    public EventServiceImpl(CategoryRepository categoryRepository, UserRepository userRepository,
                            LocationRepository locationRepository, EventRepository eventRepository, EventClient eventClient, ParticipationRequestRepository requestRepository) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.locationRepository = locationRepository;
        this.eventRepository = eventRepository;
        this.eventClient = eventClient;
        this.requestRepository = requestRepository;
    }

    public EventFullDto post(NewEventDto newEventDto, Integer userId) {
        if (newEventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new IllegalDateException("Date and time for which the event is scheduled cannot be earlier" +
                    " than two hours from the current moment"); // TODO Нужна помощь
        }
        Event newEvent = EventMapper.toEventFromNewEventDto(newEventDto);
        User initiator = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " was not found."));
        newEvent.setInitiator(initiator);
        Location eventLocation = locationRepository.save(LocationMapper.toLocation(newEventDto.getLocation()));
        newEvent.setLocation(eventLocation);
        Category eventCategory = categoryRepository.findById(newEventDto.getCategory())
                .orElseThrow(() -> new CategoryNotFoundException(
                        "Category with id " + newEventDto.getCategory() + " was not found."));
        newEvent.setCategory(eventCategory);
        return EventMapper.toEventFullDto(eventRepository.save(newEvent));
    }

    public EventFullDto cancelEvent(Integer userId, Integer eventId) {
        Event foundedEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event with id " + eventId + " was not found."));
        if (!foundedEvent.getInitiator().getId().equals(userId)) {
            throw new IllegalIdException(
                    "User with id " + userId + " is not the initiator of the event with id " + eventId);
        }
        if (!foundedEvent.getState().equals(State.PENDING.toString())) {
            throw new IllegalStateException("Only pending events can be canceled");
        }
        foundedEvent.setState(State.CANCELED.toString());
        return EventMapper.toEventFullDto(eventRepository.save(foundedEvent));
    }

    public EventFullDto publishEvent(Integer eventId) {
        LocalDateTime publishedDate = LocalDateTime.now();
        Event foundedEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event with id " + eventId + " was not found."));
        if (publishedDate.isAfter(foundedEvent.getEventDate().plusHours(1))) {

        } // TODO Нужна помощь
        if (!foundedEvent.getState().equals(State.PENDING.toString())) {
            throw new IllegalStateException("Only pending events can be published");
        }
        foundedEvent.setState(State.PUBLISHED.toString());
        foundedEvent.setPublishedOn(publishedDate);
        return EventMapper.toEventFullDto(eventRepository.save(foundedEvent));
    }

    public EventFullDto rejectEvent(Integer eventId) {
        Event foundedEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event with id " + eventId + " was not found."));
        if (!foundedEvent.getState().equals(State.PENDING.toString())) {
            throw new IllegalStateException("Only pending events can be canceled");
        }
        foundedEvent.setState(State.CANCELED.toString());
        return EventMapper.toEventFullDto(eventRepository.save(foundedEvent));
    }

    public EventFullDto getEventByIdPublic(Integer eventId, String ip, String uri) {
        Event foundedEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event with id " + eventId + " was not found."));
        EventFullDto eventDto = EventMapper.toEventFullDto(foundedEvent);
        if (!eventDto.getState().equals(State.PUBLISHED.toString())) {
            throw new IllegalStateException("Only published events can be viewed");
        }
        // Сохранение в сервис статистики
        eventClient.postRequest(new EndpointHit(null, "ewn", uri, ip, LocalDateTime.now()));
        // Получаем все подтвержденные запрос и заполняем поле
        eventDto.setConfirmedRequests(requestRepository.findAllByEventAndStatusIs(
                eventId, Status.APPROVED.toString()).size());
        eventDto.setViews(Library.getViews(uri, eventRepository, eventClient));
        return eventDto;
    }

    public List<EventShortDto> getAllUsersEvents(Integer userId, Integer from, Integer size) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User with id " + userId + " was not found.");
        }
        PageRequest pageRequest = PageRequest.of(from / size, size, Sort.by("id"));
        List<EventShortDto> eventShortDtoList = eventRepository.findAllByInitiatorId(userId, pageRequest).stream()
                .map(EventMapper::toEventShortDto).collect(Collectors.toList());
        for (EventShortDto eventShortDto : eventShortDtoList) {
            eventShortDto.setConfirmedRequests(requestRepository
                    .findAllByEventAndStatusIs(eventShortDto.getId(), Status.APPROVED.toString()).size());
            String uri = "/events/" + eventShortDto.getId();
            eventShortDto.setViews(Library.getViews(uri, eventRepository, eventClient));
        }
        return eventShortDtoList;
    }

    public EventFullDto getUserEvent(Integer userId, Integer eventId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User with id " + userId + " was not found.");
        }
        Event foundedEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event with id " + eventId + " was not found."));
        if (!foundedEvent.getInitiator().getId().equals(userId)) {
            throw new IllegalIdException(
                    "User with id " + userId + " is not the initiator of the event with id " + eventId);
        }
        EventFullDto eventFullDto = EventMapper.toEventFullDto(foundedEvent);
        eventFullDto.setConfirmedRequests(requestRepository.findAllByEventAndStatusIs(
                eventId, Status.APPROVED.toString()).size());
        String uri = "/events/" + eventId;
        eventFullDto.setViews(Library.getViews(uri, eventRepository, eventClient));
        return eventFullDto;
    }

    public EventFullDto updateEventByUser(Integer userId, UpdateEventRequest updateEventRequest) {
        Event foundedEvent = eventRepository.findById(updateEventRequest.getEventId())
                .orElseThrow(() -> new EventNotFoundException(
                        "Event with id " + updateEventRequest.getEventId() + " was not found."));
        Category foundedCategory = categoryRepository.findById(updateEventRequest.getCategory())
                .orElseThrow(() -> new CategoryNotFoundException(
                        "Category with id " + updateEventRequest.getCategory() + " was not found."));
        if (foundedEvent.getState().equals(State.PUBLISHED.toString())) {
            throw new InvalidAccessException("To cancel an event, the status must be either PENDING or CANCELED");
        }
        if (foundedEvent.getState().equals(State.CANCELED.toString())) {
            foundedEvent.setState(State.PENDING.toString());
        }
        if (updateEventRequest.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new IllegalDateException("Date and time for which the event is scheduled cannot be earlier" +
                    " than two hours from the current moment");
        }
        foundedEvent.setAnnotation(updateEventRequest.getAnnotation());
        foundedEvent.setCategory(foundedCategory);
        foundedEvent.setDescription(updateEventRequest.getDescription());
        foundedEvent.setEventDate(updateEventRequest.getEventDate());
        foundedEvent.setPaid(updateEventRequest.getPaid());
        foundedEvent.setParticipantLimit(updateEventRequest.getParticipantLimit());
        Integer confirmedRequestsNumber = requestRepository
                .findAllByEventAndStatusIs(foundedEvent.getId(), Status.APPROVED.toString()).size();
        if (foundedEvent.getParticipantLimit() < confirmedRequestsNumber) {
            throw new InvalidAccessException("The value in the updated \"participantLimit\" field cannot be less" +
                    " than confirmed requests of the event");
        }
        foundedEvent.setTitle(updateEventRequest.getTitle());
        EventFullDto eventFullDto = EventMapper.toEventFullDto(eventRepository.save(foundedEvent));
        eventFullDto.setConfirmedRequests(confirmedRequestsNumber);
        String uri = "/events/" + eventFullDto.getId();
        eventFullDto.setViews(Library.getViews(uri, eventRepository, eventClient));
        return eventFullDto;
    }

    public EventFullDto putEventByAdmin(Integer eventId, AdminUpdateEventRequest adminUpdateEventRequest) {
        Category foundedCategory = categoryRepository.findById(adminUpdateEventRequest.getCategory())
                .orElseThrow(() -> new CategoryNotFoundException(
                        "Category with id " + adminUpdateEventRequest.getCategory() + " was not found."));
        Event foundedEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event with id " + eventId + " was not found."));
        Event updatedEvent = EventMapper.toEventFromAdminUpdateEventRequest(adminUpdateEventRequest);

        Integer confirmedRequestsNumber = requestRepository
                .findAllByEventAndStatusIs(eventId, Status.APPROVED.toString()).size();
        // Я не мог этого не провалидировать
        if (updatedEvent.getParticipantLimit() < confirmedRequestsNumber) {
            throw new InvalidAccessException("The value in the updated \"participantLimit\" field cannot be less" +
                    " than confirmed requests of the event");
        }

        updatedEvent.setId(foundedEvent.getId());
        updatedEvent.setCategory(foundedCategory);
        updatedEvent.setCreatedOn(foundedEvent.getCreatedOn());
        updatedEvent.setInitiator(foundedEvent.getInitiator());
        updatedEvent.getLocation().setId(foundedEvent.getLocation().getId());
        updatedEvent.setPublishedOn(foundedEvent.getPublishedOn());
        updatedEvent.setState(foundedEvent.getState());
        EventFullDto eventFullDto = EventMapper.toEventFullDto(eventRepository.save(updatedEvent));
        eventFullDto.setConfirmedRequests(confirmedRequestsNumber);
        String uri = "/events/" + eventFullDto.getId();
        eventFullDto.setViews(Library.getViews(uri, eventRepository, eventClient));
        return eventFullDto;
    }

    public List<EventFullDto> getEventsByAdmin(List<Integer> users, List<String> states, List<Integer> categories,
                                               LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from,
                                               Integer size) {

        PageRequest pageRequest = PageRequest.of(from / size, size, Sort.by("id"));
        List<EventFullDto> foundedEvents = eventRepository.getEvents(rangeStart, rangeEnd, users, states, categories,
                        pageRequest).stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());
        for (EventFullDto eventFullDto : foundedEvents) {
            Integer confirmedRequestsNumber = requestRepository
                    .findAllByEventAndStatusIs(eventFullDto.getId(), Status.APPROVED.toString()).size();
            eventFullDto.setConfirmedRequests(confirmedRequestsNumber);
            String uri = "/events/" + eventFullDto.getId();
            eventFullDto.setViews(Library.getViews(uri, eventRepository, eventClient));
        }
        return foundedEvents;
    }

    public List<EventFullDto> getPublicEvents(String text, List<Integer> categories, Boolean paid, LocalDateTime rangeStart,
                                              LocalDateTime rangeEnd, Boolean onlyAvailable, String sort,
                                              Integer from, Integer size, String ip, String savedUri) {

        List<EventFullDto> events = new ArrayList<>();
        if (rangeStart == null && rangeEnd == null) {
            if (sort.equals("EVENT_DATE")) {
                PageRequest pageRequest = PageRequest.of(from / size, size, Sort.by("event_date"));
                events = getPublicEventsWithoutDates(events, text, categories, paid, pageRequest);
            } else if (sort.equals("VIEWS")) {
                PageRequest pageRequest = PageRequest.of(from / size, size);
                events = getPublicEventsWithoutDates(events, text, categories, paid, pageRequest);
                events.stream().sorted().collect(Collectors.toList());
            }
        }
        if (sort.equals("EVENT_DATE")) {
            PageRequest pageRequest = PageRequest.of(from / size, size, Sort.by("event_date"));
            events = eventRepository.getPublicEvents(text, categories, paid, rangeStart, rangeEnd, pageRequest).stream()
                    .map(EventMapper::toEventFullDto)
                    .collect(Collectors.toList());
            for (EventFullDto eventFullDto : events) {
                Integer confirmedRequestsNumber = requestRepository
                        .findAllByEventAndStatusIs(eventFullDto.getId(), Status.APPROVED.toString()).size();
                eventFullDto.setConfirmedRequests(confirmedRequestsNumber);
                String uri = "/events/" + eventFullDto.getId();
                eventFullDto.setViews(Library.getViews(uri, eventRepository, eventClient));
            }
        } else if (sort.equals("VIEWS")) {
            PageRequest pageRequest = PageRequest.of(from / size, size);
            events = getPublicEventsWithoutDates(events, text, categories, paid, pageRequest);
            events.stream().sorted().collect(Collectors.toList());
        }
        return events; // TODO Закончить
    }


    private List<EventFullDto> getPublicEventsWithoutDates(List<EventFullDto> events, String text,
                                                           List<Integer> categories, Boolean paid, PageRequest pageRequest) {
        events = eventRepository.getPublicEventsWithoutDates(text, categories, paid, LocalDateTime.now(),
                        pageRequest).stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());
        for (EventFullDto eventFullDto : events) {
            Integer confirmedRequestsNumber = requestRepository
                    .findAllByEventAndStatusIs(eventFullDto.getId(), Status.APPROVED.toString()).size();
            eventFullDto.setConfirmedRequests(confirmedRequestsNumber);
            String uri = "/events/" + eventFullDto.getId();
            eventFullDto.setViews(Library.getViews(uri, eventRepository, eventClient));
        }
        return events;
    }
}
