package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.exception.*;
import ru.practicum.mapper.CommentMapper;
import ru.practicum.model.Comment;
import ru.practicum.model.Event;
import ru.practicum.model.State;
import ru.practicum.model.dto.CommentDto;
import ru.practicum.repository.CommentRepository;
import ru.practicum.service.interfaces.CommentService;
import ru.practicum.service.interfaces.EventService;
import ru.practicum.service.interfaces.UserService;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final EventService eventService;
    private final UserService userService;

    public CommentDto postComment(Integer userId, Integer eventId, CommentDto commentDto) {
        if (!userService.existsById(userId)) {
            throw new NotFoundException("User with id " + userId + " was not found.");
        }
        Event foundedEvent = eventService.findById(eventId);
        if (!foundedEvent.getState().equals(State.PUBLISHED)) {
            throw new InvalidAccessException("Only published events can be commented");
        }
        commentDto.setCreatedOn(LocalDateTime.now());
        commentDto.setUserId(userId);
        commentDto.setEventId(eventId);
        return CommentMapper.toCommentDto(commentRepository.save(CommentMapper.toComment(commentDto)));
    }

    public Page<CommentDto> getEventComments(Integer eventId, Integer from, Integer size) {
        if (!eventService.existsById(eventId)) {
            throw new NotFoundException("Event with id " + eventId + " was not found.");
        }
        PageRequest pageRequest = PageRequest.of(from, size, Sort.by("createdOn").descending());
        return commentRepository.findAllByEventId(eventId, pageRequest)
                .map(CommentMapper::toCommentDto);
    }

    public CommentDto updateComment(CommentDto commentDto) {
        Comment foundedComment = commentRepository.findById(commentDto.getId())
                .orElseThrow(() -> new NotFoundException("Comment with id " + commentDto.getId() +
                        " was not found."));
        if (commentDto.getText() != null) {
            foundedComment.setText(commentDto.getText());
        }
        return CommentMapper.toCommentDto(commentRepository.save(foundedComment));
    }
}
