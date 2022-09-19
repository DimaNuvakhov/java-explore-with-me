package ru.practicum.service;

import org.springframework.stereotype.Service;
import ru.practicum.exception.*;
import ru.practicum.exception.IllegalStateException;
import ru.practicum.mapper.ParticipationRequestMapper;
import ru.practicum.model.*;
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
            throw new ParticipationRequestNotFoundException("Request with requester Id" + userId + " and event Id "
                    + eventId + " was not found.");
        }
        requestDto.setCreated(LocalDateTime.now());
        Event foundedEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event with id " + eventId + " was not found."));
        User requester = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " was not found."));
        if (foundedEvent.getInitiator().getId().equals(requester.getId())) {
            throw new InvalidAccessException("Initiator of the event cannot add a request to participate in his event.");
        }
        if (!foundedEvent.getState().equals(State.PUBLISHED.toString())) {
            throw new IllegalStateException("Only a published event can accept participation requests.");
        }
        Integer confirmedRequests = requestRepository
                .findAllByEventAndStatusIs(foundedEvent.getId(), Status.APPROVED.toString()).size();
        if (foundedEvent.getParticipantLimit().equals(confirmedRequests)) {
            throw new ParticipantLimitException("Limit of requests for participation has been reached");
        }
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
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User with id " + userId + " was not found.");
        }
        Event foundedEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event with id " + eventId + " was not found."));
        if (!foundedEvent.getInitiator().getId().equals(userId)) {
            throw new IllegalIdException(
                    "User with id " + userId + " is not the initiator of the event with id " + eventId);
        }
        return ParticipationRequestMapper.toRequestDtoList(requestRepository.findAllByEvent(foundedEvent.getId()));
    }

    public ParticipationRequestDto cancelUserRequest(Integer userId, Integer requestId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User with id " + userId + " was not found.");
        }
        ParticipationRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new ParticipationRequestNotFoundException(
                        "Request with id " + requestId + " was not found."));
        if (!request.getRequester().equals(userId)) {
            throw new InvalidAccessException(
                    "User with id " + userId + " is not the requester of the request with id " + requestId);
        }
        request.setStatus(Status.REJECTED.toString());
        return ParticipationRequestMapper
                .toParticipationRequestDto(requestRepository.save(request));
    }

}
