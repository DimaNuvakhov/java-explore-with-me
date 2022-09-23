package ru.practicum.service.interfaces;

import ru.practicum.model.dto.ParticipationRequestDto;

import java.util.List;

public interface ParticipationRequestService {

    ParticipationRequestDto post(Integer userId, Integer eventId);

    List<ParticipationRequestDto> getAllUserRequests(Integer userId);

    List<ParticipationRequestDto> getAllUserEventRequests(Integer userId, Integer eventId);

    ParticipationRequestDto cancelUserRequest(Integer userId, Integer requestId);

    ParticipationRequestDto confirmRequest(Integer userId, Integer eventId, Integer reqId);

    ParticipationRequestDto rejectRequest(Integer userId, Integer eventId, Integer reqId);
}
