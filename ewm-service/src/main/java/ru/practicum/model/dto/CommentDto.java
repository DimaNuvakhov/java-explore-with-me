package ru.practicum.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentDto {

    private Integer id;

    private String text;

    private Integer userId;

    private Integer eventId;

    private LocalDateTime createdOn;

    public CommentDto(Integer id, String text, Integer userId, Integer eventId, LocalDateTime createdOn) {
        this.id = id;
        this.text = text;
        this.userId = userId;
        this.eventId = eventId;
        this.createdOn = createdOn;
    }
}
