package ru.practicum.service.interfaces;

import ru.practicum.model.dto.ParticipationRequestDto;

import java.util.List;

public interface ParticipationRequestService {

    ParticipationRequestDto post(Integer userId, Integer eventId, ParticipationRequestDto requestDto);

    List<ParticipationRequestDto> getAllUserRequests(Integer userId);

    List<ParticipationRequestDto> getAllUserEventRequests(Integer userId, Integer eventId);

}
