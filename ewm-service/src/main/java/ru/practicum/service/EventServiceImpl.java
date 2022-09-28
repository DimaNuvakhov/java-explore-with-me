package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.client.EventClient;
import ru.practicum.commonLibrary.Library;
import ru.practicum.exception.*;
import ru.practicum.exception.IllegalStateException;
import ru.practicum.mapper.EventMapper;
import ru.practicum.mapper.LocationMapper;
import ru.practicum.model.*;
import ru.practicum.model.dto.*;
import ru.practicum.repository.*;
import ru.practicum.service.interfaces.EventService;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class EventServiceImpl implements EventService {

    private final CategoryRepository categoryRepository;

    private final UserRepository userRepository;

    private final LocationRepository locationRepository;

    private final EventRepository eventRepository;

    private final EventClient eventClient;

    private final ParticipationRequestRepository requestRepository;

    public EventFullDto post(NewEventDto newEventDto, Integer userId) {
        if (newEventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new IllegalDateException("Date and time for which the event is scheduled cannot be earlier" +
                    " than two hours from the current moment");
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
        if (publishedDate.isAfter(foundedEvent.getEventDate().minusHours(1))) {
            throw new InvalidAccessException("The start date of the event must be no earlier than one hour " +
                    "from the date of publication");
        }
        if (!foundedEvent.getState().equals(State.PENDING.toString())) {
            throw new IllegalStateException("Only pending events can be published");
        }
        foundedEvent.setState(State.PUBLISHED.toString());
        foundedEvent.setPublishedOn(publishedDate);
        EventFullDto eventFullDto = EventMapper.toEventFullDto(eventRepository.save(foundedEvent));
        eventFullDto.setConfirmedRequests(requestRepository.findAllByEventAndStatusIs(
                eventId, Status.CONFIRMED.toString()).size());
        String uri = "/events/" + eventId;
        eventFullDto.setViews(Library.getViews(uri, eventRepository, eventClient));
        return eventFullDto;
    }

    public EventFullDto rejectEvent(Integer eventId) {
        Event foundedEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event with id " + eventId + " was not found."));
        if (!foundedEvent.getState().equals(State.PENDING.toString())) {
            throw new IllegalStateException("Only pending events can be canceled");
        }
        foundedEvent.setState(State.CANCELED.toString());
        EventFullDto eventFullDto = EventMapper.toEventFullDto(eventRepository.save(foundedEvent));
        eventFullDto.setConfirmedRequests(requestRepository.findAllByEventAndStatusIs(
                eventId, Status.CONFIRMED.toString()).size());
        String uri = "/events/" + eventId;
        eventFullDto.setViews(Library.getViews(uri, eventRepository, eventClient));
        return eventFullDto;
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
                eventId, Status.CONFIRMED.toString()).size());
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
                    .findAllByEventAndStatusIs(eventShortDto.getId(), Status.CONFIRMED.toString()).size());
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
                eventId, Status.CONFIRMED.toString()).size());
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
                .findAllByEventAndStatusIs(foundedEvent.getId(), Status.CONFIRMED.toString()).size();
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
                .findAllByEventAndStatusIs(eventId, Status.CONFIRMED.toString()).size();
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
                    .findAllByEventAndStatusIs(eventFullDto.getId(), Status.CONFIRMED.toString()).size();
            eventFullDto.setConfirmedRequests(confirmedRequestsNumber);
            String uri = "/events/" + eventFullDto.getId();
            eventFullDto.setViews(Library.getViews(uri, eventRepository, eventClient));
        }
        return foundedEvents;
    }

    public List<EventFullDto> getPublicEvents(String text, List<Integer> categories, Boolean paid, LocalDateTime rangeStart,
                                              LocalDateTime rangeEnd, Boolean onlyAvailable, String sort,
                                              Integer from, Integer size, String ip, String savedUri) {
        // Сохраняю информацию о том, что я дернул эту ручку в сервис статистики
        eventClient.postRequest(new EndpointHit(null, "ewn", savedUri, ip, LocalDateTime.now()));
        List<EventFullDto> events;
        PageRequest pageRequest = PageRequest.of(from / size, size);
        if (rangeStart == null && rangeEnd == null) {
            events = eventRepository.getPublicEventsWithoutDates(text, categories, paid, pageRequest)
                    .stream()
                    .map(EventMapper::toEventFullDto)
                    .collect(Collectors.toList());
        } else {
            events = eventRepository.getPublicEvents(text, categories, paid, rangeStart, rangeEnd, pageRequest)
                    .stream()
                    .map(EventMapper::toEventFullDto)
                    .collect(Collectors.toList());
        }
        if (onlyAvailable) {
            for (EventFullDto eventFullDto : events) {
                Integer confirmedRequestsNumber = requestRepository
                        .findAllByEventAndStatusIs(eventFullDto.getId(), Status.CONFIRMED.toString()).size();
                if (eventFullDto.getParticipantLimit().equals(confirmedRequestsNumber)) {
                    events.remove(eventFullDto);
                }
            }
        }
        for (EventFullDto eventFullDto : events) {
            Integer confirmedRequestsNumber = requestRepository
                    .findAllByEventAndStatusIs(eventFullDto.getId(), Status.CONFIRMED.toString()).size();
            eventFullDto.setConfirmedRequests(confirmedRequestsNumber);
            String uri = "/events/" + eventFullDto.getId();
            eventFullDto.setViews(Library.getViews(uri, eventRepository, eventClient));
        }
        if (sort.equals("EVENT_DATE")) {
            events.sort(Comparator.comparing(EventFullDto::getEventDate).reversed());
        } else if (sort.equals("VIEWS")) {
            events.sort(Comparator.comparing(EventFullDto::getViews).reversed());
        }
        return events;
    }
}
