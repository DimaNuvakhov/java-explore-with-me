package ru.practicum.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.model.ParticipationRequest;
import ru.practicum.model.dto.ParticipationRequestDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ParticipationRequestMapper {

    public static ParticipationRequestDto toParticipationRequestDto(ParticipationRequest request) {
        return new ParticipationRequestDto(request.getId(), request.getCreated(), request.getEvent(),
        request.getRequester(), request.getStatus());
    }
}
