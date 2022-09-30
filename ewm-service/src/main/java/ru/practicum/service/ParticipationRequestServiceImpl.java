package ru.practicum.service;

import lombok.RequiredArgsConstructor;
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
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ParticipationRequestServiceImpl implements ParticipationRequestService {

    private final ParticipationRequestRepository requestRepository;

    private final UserRepository userRepository;

    private final EventRepository eventRepository;

    public ParticipationRequestDto post(Integer userId, Integer eventId) {
        if (requestRepository.existsByRequesterAndAndEvent(userId, eventId)) {
            throw new ParticipationRequestNotFoundException("Request with requester Id" + userId + " and event Id "
                    + eventId + " was not found.");
        }
        ParticipationRequest request = new ParticipationRequest();
        request.setCreated(LocalDateTime.now());
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
                .findAllByEventAndStatusIs(foundedEvent.getId(), Status.CONFIRMED.toString()).size();
        if (foundedEvent.getParticipantLimit().equals(confirmedRequests)) {
            throw new ParticipantLimitException("Limit of requests for participation has been reached");
        }
        if (!foundedEvent.getRequestModeration()) {
            request.setStatus(Status.CONFIRMED.toString());
        }
        request.setStatus(Status.PENDING.toString());
        request.setEvent(foundedEvent.getId());
        request.setRequester(requester.getId());
        return ParticipationRequestMapper
                .toParticipationRequestDto(requestRepository.save((request)));
    }

    public List<ParticipationRequestDto> getAllUserRequests(Integer userId) {
        return requestRepository.findAllByRequester(userId).stream()
                .map(ParticipationRequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
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
        return requestRepository.findAllByEvent(foundedEvent.getId()).stream()
                .map(ParticipationRequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    public ParticipationRequestDto cancelUserRequest(Integer userId, Integer reqId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User with id " + userId + " was not found.");
        }
        ParticipationRequest request = requestRepository.findById(reqId)
                .orElseThrow(() -> new ParticipationRequestNotFoundException(
                        "Request with id " + reqId + " was not found."));
        if (!request.getRequester().equals(userId)) {
            throw new InvalidAccessException(
                    "User with id " + userId + " is not the requester of the request with id " + reqId);
        }
        request.setStatus(Status.CANCELED.toString());
        return ParticipationRequestMapper
                .toParticipationRequestDto(requestRepository.save(request));
    }

    public ParticipationRequestDto confirmRequest(Integer userId, Integer eventId, Integer reqId) {
        Event foundedEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event with id " + eventId + " was not found."));
        ParticipationRequest foundedParticipationRequest = requestRepository.findById(reqId)
                .orElseThrow(() -> new ParticipationRequestNotFoundException(
                        "Request with id " + reqId + " was not found."));
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User with id " + userId + " was not found.");
        }
        if (!foundedEvent.getInitiator().getId().equals(userId)) {
            throw new InvalidAccessException(
                    "User with id " + userId + " is not the initiator of the event with id " + eventId);
        }
        if (foundedParticipationRequest.getRequester().equals(userId)) {
            throw new InvalidAccessException("User can not participate in his own event");
        }
        if (foundedEvent.getParticipantLimit() == 0 || !foundedEvent.getRequestModeration()) {
            foundedParticipationRequest.setStatus(Status.CONFIRMED.toString());
        }
        Integer confirmedRequestsNumber = requestRepository
                .findAllByEventAndStatusIs(eventId, Status.CONFIRMED.toString()).size();
        if (foundedEvent.getParticipantLimit().equals(confirmedRequestsNumber)) {
            foundedParticipationRequest.setStatus(Status.REJECTED.toString());
            requestRepository.save(foundedParticipationRequest);
        }
        foundedParticipationRequest.setStatus(Status.CONFIRMED.toString());
        requestRepository.save(foundedParticipationRequest);
        if (foundedEvent.getParticipantLimit().equals(confirmedRequestsNumber)) {
            List<ParticipationRequest> participationRequestList = requestRepository
                    .findAllByEventAndStatusIs(eventId, Status.PENDING.toString());
            for (ParticipationRequest participationRequest : participationRequestList) {
                participationRequest.setStatus(Status.REJECTED.toString());
                requestRepository.save(participationRequest);
            }
        }
        return ParticipationRequestMapper.toParticipationRequestDto(foundedParticipationRequest);
    }

    public ParticipationRequestDto rejectRequest(Integer userId, Integer eventId, Integer reqId) {
        Event foundedEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event with id " + eventId + " was not found."));
        ParticipationRequest foundedParticipationRequest = requestRepository.findById(reqId)
                .orElseThrow(() -> new ParticipationRequestNotFoundException(
                        "Request with id " + reqId + " was not found."));
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User with id " + userId + " was not found.");
        }
        if (!foundedEvent.getInitiator().getId().equals(userId)) {
            throw new InvalidAccessException(
                    "User with id " + userId + " is not the initiator of the event with id " + eventId);
        }
        if (foundedParticipationRequest.getRequester().equals(userId)) {
            throw new InvalidAccessException("User can not participate in his own event");
        }
        if (!foundedParticipationRequest.getStatus().equals(Status.PENDING.toString())) {
            throw new InvalidAccessException("Only pending requests can be rejected");
        }
        foundedParticipationRequest.setStatus(Status.REJECTED.toString());
        return ParticipationRequestMapper.toParticipationRequestDto(requestRepository.save(foundedParticipationRequest));
    }
}
