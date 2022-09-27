package ru.practicum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.exception.CommentNotFoundException;
import ru.practicum.exception.EventNotFoundException;
import ru.practicum.exception.UserNotFoundException;
import ru.practicum.mapper.CommentMapper;
import ru.practicum.model.Comment;
import ru.practicum.model.dto.CommentDto;
import ru.practicum.repository.CommentRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.UserRepository;
import ru.practicum.service.interfaces.CommentService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, EventRepository eventRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    public CommentDto post(Integer userId, Integer eventId, CommentDto commentDto) {
        if (userRepository.existsById(userId)) {
            throw new UserNotFoundException("User with id " + userId + " was not found.");
        }
        if (eventRepository.existsById(eventId)) {
            throw new EventNotFoundException("Event with id " + eventId + " was not found.");
        }
        commentDto.setCreatedOn(LocalDateTime.now());
        commentDto.setUserId(userId);
        commentDto.setEventId(eventId);
        return CommentMapper.toCommentDto(commentRepository.save(CommentMapper.toComment(commentDto)));
    }

    public List<CommentDto> getEventComments(Integer eventId, Integer from, Integer size) {
        if (eventRepository.existsById(eventId)) {
            throw new EventNotFoundException("Event with id " + eventId + " was not found.");
        }
        PageRequest pageRequest = PageRequest.of(from / size, size, Sort.by("created_on"));
        return commentRepository.findAllByEventId(eventId, pageRequest).stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    public CommentDto patch(CommentDto commentDto) {
        Comment foundedComment = commentRepository.findById(commentDto.getId())
                .orElseThrow(() -> new CommentNotFoundException("Comment with id " + commentDto.getId() +
                        " was not found."));
        if (commentDto.getText() != null) {
            foundedComment.setText(commentDto.getText());
        }
        return CommentMapper.toCommentDto(commentRepository.save(foundedComment));
    }
}
