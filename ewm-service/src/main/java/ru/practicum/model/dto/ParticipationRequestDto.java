package ru.practicum.model.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
public class ParticipationRequestDto {

    private Integer id;

    private LocalDateTime created;

    private Integer event;

    @NotNull
    private Integer requester;

    private String status;

    public ParticipationRequestDto(Integer id, LocalDateTime created, Integer event, Integer requester, String status) {
        this.id = id;
        this.created = created;
        this.event = event;
        this.requester = requester;
        this.status = status;
    }
}
