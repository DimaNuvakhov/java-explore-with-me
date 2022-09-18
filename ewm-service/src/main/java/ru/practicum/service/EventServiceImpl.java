package ru.practicum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.client.EventClient;
import ru.practicum.mapper.EventMapper;
import ru.practicum.mapper.LocationMapper;
import ru.practicum.model.*;
import ru.practicum.model.dto.EventFullDto;
import ru.practicum.model.dto.NewEventDto;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.LocationRepository;
import ru.practicum.repository.UserRepository;
import ru.practicum.service.interfaces.EventService;

import java.time.LocalDateTime;

@Service
public class EventServiceImpl implements EventService {

    private final CategoryRepository categoryRepository;

    private final UserRepository userRepository;

    private final LocationRepository locationRepository;

    private final EventRepository eventRepository;

    private final EventClient eventClient;

    @Autowired
    public EventServiceImpl(CategoryRepository categoryRepository, UserRepository userRepository,
                            LocationRepository locationRepository, EventRepository eventRepository, EventClient eventClient) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.locationRepository = locationRepository;
        this.eventRepository = eventRepository;
        this.eventClient = eventClient;
    }

    public EventFullDto post(NewEventDto newEventDto, Integer userId) {
        if (newEventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            // TODO Бросить исключение!
        }
        Event newEvent = EventMapper.toEventFromNewEventDto(newEventDto);
        User initiator = userRepository.findById(userId).orElse(new User()); // TODO Здесь нужно кинуть исключение
        newEvent.setInitiator(initiator);
        Location eventLocation = locationRepository.save(LocationMapper.toLocation(newEventDto.getLocation()));
        newEvent.setLocation(eventLocation);
        Category eventCategory = categoryRepository.findById(newEventDto.getCategory()).orElse(new Category());
        // TODO Здесь нужно кинуть исключение
        newEvent.setCategory(eventCategory);
        return EventMapper.toEventFullDto(eventRepository.save(newEvent));
    }

    public EventFullDto cancelEvent(Integer userId, Integer eventId) {
        Event foundedEvent = eventRepository.findById(eventId).orElse(new Event()); // TODO Здесь нужно кинуть исключение
        if (!foundedEvent.getInitiator().getId().equals(userId)) {
            // TODO Здесь нужно кинуть исключение
        }
        if (!foundedEvent.getState().equals(State.PENDING.toString())) {
            // TODO Здесь нужно кинуть исключение
        }
        foundedEvent.setState(State.CANCELED.toString());
        return EventMapper.toEventFullDto(eventRepository.save(foundedEvent));
    }

    public EventFullDto getEventByIdPublic(Integer eventId, String ip, String uri) {
        Event foundedEvent = eventRepository.findById(eventId).orElse(new Event()); // TODO Здесь нужно кинуть исключение
        if (!foundedEvent.getState().equals(State.PUBLISHED.toString())) {
            // TODO Здесь нужно кинуть исключение
        }
        eventClient.postRequest(new EndpointHit(null, "ewn", uri, ip, LocalDateTime.now()));
        // TODO Доделать!!!

        return null;
    }
}
