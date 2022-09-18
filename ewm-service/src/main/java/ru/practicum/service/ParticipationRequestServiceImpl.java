package ru.practicum.service;

import org.springframework.stereotype.Service;
import ru.practicum.exception.EventNotFoundException;
import ru.practicum.exception.UserNotFoundException;
import ru.practicum.mapper.ParticipationRequestMapper;
import ru.practicum.model.Event;
import ru.practicum.model.State;
import ru.practicum.model.Status;
import ru.practicum.model.User;
import ru.practicum.model.dto.ParticipationRequestDto;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.ParticipationRequestRepository;
import ru.practicum.repository.UserRepository;
import ru.practicum.service.interfaces.ParticipationRequestService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ParticipationRequestServiceImpl implements ParticipationRequestService {

    private final ParticipationRequestRepository requestRepository;

    private final UserRepository userRepository;

    private final EventRepository eventRepository;

    public ParticipationRequestServiceImpl(ParticipationRequestRepository requestRepository, UserRepository userRepository, EventRepository eventRepository) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    public ParticipationRequestDto post(Integer userId, Integer eventId, ParticipationRequestDto requestDto) {
        if (requestRepository.existsByRequesterAndAndEvent(userId, eventId)) {
            // TODO нужно кинуть исключение
        }
        requestDto.setCreated(LocalDateTime.now());
        Event foundedEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event with id " + eventId + " was not found."));
        User requester = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " was not found."));
        if (foundedEvent.getInitiator().getId().equals(requester.getId())) {
            // TODO Здесь нужно кинуть исключение
        }
        if (!foundedEvent.getState().equals(State.PUBLISHED.toString())) {
            // TODO Здесь нужно кинуть исключение
        }
//        Integer limit = foundedEvent.getParticipantLimit();
//        if (foundedEvent.getParticipantLimit().equals(limit)) {
//            // TODO Здесь нужно кинуть исключение
//        } // TODO Доделать
        if (!foundedEvent.getRequestModeration()) {
            requestDto.setStatus(Status.APPROVED.toString());
        }
        requestDto.setStatus(Status.PENDING.toString());
        return ParticipationRequestMapper
                .toParticipationRequestDto(requestRepository.save(ParticipationRequestMapper
                        .toParticipationRequest(requestDto)));
    }

    public List<ParticipationRequestDto> getAllUserRequests(Integer userId) {
        return null;
    }

    public List<ParticipationRequestDto> getAllUserEventRequests(Integer userId, Integer eventId) {
        Event foundedEvent = eventRepository.findById(eventId).orElse(new Event()); // TODO Здесь нужно кинуть исключение
        User initiator = userRepository.findById(userId).orElse(new User()); // TODO Здесь нужно кинуть исключение
        if (!foundedEvent.getInitiator().getId().equals(initiator.getId())) {
            // TODO Оба айдишника должны совпадать
        }
        return ParticipationRequestMapper.toRequestDtoList(requestRepository.findAllByEvent(foundedEvent.getId()));
    }
}
