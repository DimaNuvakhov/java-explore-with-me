package ru.practicum.service.interfaces;

import org.springframework.data.domain.Page;
import ru.practicum.model.dto.CommentDto;

public interface CommentService {

    CommentDto postComment(Integer userId, Integer eventId, CommentDto commentDto);

    Page<CommentDto> getEventComments(Integer eventId, Integer from, Integer size);

    CommentDto updateComment(CommentDto commentDto);
}
