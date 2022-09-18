package ru.practicum.mapper;

import ru.practicum.model.ParticipationRequest;
import ru.practicum.model.dto.ParticipationRequestDto;

import java.util.ArrayList;
import java.util.List;

public class ParticipationRequestMapper {

    public static ParticipationRequestDto toParticipationRequestDto(ParticipationRequest request) {
        return new ParticipationRequestDto(request.getId(), request.getCreated(), request.getEvent(),
        request.getRequester(), request.getStatus());
    }

    public static ParticipationRequest toParticipationRequest(ParticipationRequestDto requestDto) {
        return new ParticipationRequest(requestDto.getId(), requestDto.getCreated(), requestDto.getEvent(),
                requestDto.getRequester(), requestDto.getStatus());
    }

    public static List<ParticipationRequestDto> toRequestDtoList(List<ParticipationRequest> requests) {
        List<ParticipationRequestDto> requestDtos = new ArrayList<>();
        for (ParticipationRequest request : requests) {
            requestDtos.add(toParticipationRequestDto(request));
        }
        return requestDtos;
    }
}
