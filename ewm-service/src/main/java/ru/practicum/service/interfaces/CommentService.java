package ru.practicum.service.interfaces;

import ru.practicum.model.dto.CommentDto;

import java.util.List;

public interface CommentService {

    CommentDto post(Integer userId, Integer eventId, CommentDto commentDto);

    List<CommentDto> getEventComments(Integer eventId, Integer from, Integer size);

    CommentDto patch(CommentDto commentDto);
}
