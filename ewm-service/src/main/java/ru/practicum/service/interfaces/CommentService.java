package ru.practicum.service.interfaces;

import ru.practicum.model.dto.CommentDto;

import java.util.List;

public interface CommentService {

    CommentDto postComment(Integer userId, Integer eventId, CommentDto commentDto);

    List<CommentDto> getEventComments(Integer eventId, Integer from, Integer size);

    CommentDto updateComment(CommentDto commentDto);
}
