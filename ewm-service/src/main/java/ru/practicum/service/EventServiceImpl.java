package ru.practicum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.client.EventClient;
import ru.practicum.exception.*;
import ru.practicum.exception.IllegalStateException;
import ru.practicum.mapper.EventMapper;
import ru.practicum.mapper.LocationMapper;
import ru.practicum.model.*;
import ru.practicum.model.dto.EventFullDto;
import ru.practicum.model.dto.EventShortDto;
import ru.practicum.model.dto.NewEventDto;
import ru.practicum.repository.*;
import ru.practicum.service.interfaces.EventService;

import java.time.LocalDateTime;
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
        eventDto.setViews(getViewsRequest(uri));
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
            eventShortDto.setViews(getViewsRequest(uri));
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
        eventFullDto.setViews(getViewsRequest(uri));
        return eventFullDto;
    }

    private Integer getViewsRequest(String uri) {
        // Получаем минимальную дату
        LocalDateTime start = eventRepository.findMinPublishedOn(); // TODO Разобратсья что передавать
        // Получаем максимальную дату
        LocalDateTime end = eventRepository.findMaxPublishedOn(); // TODO Разобратсья что передавать
        // Формируес список uri
        // Формируем запрос для получения данных с сервиса статистики
        ResponseEntity<Object> list = eventClient.getRequest(start.minusDays(1), end.plusDays(1), uri, false);
        List<Map<String, Object>> statsList = (List<Map<String, Object>>) list.getBody();
        Map<String, Object> statsMap = statsList.get(0);
        return (Integer) statsMap.get("hits");
    }
}
