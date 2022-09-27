package ru.practicum.mapper;

import ru.practicum.model.Comment;
import ru.practicum.model.dto.CommentDto;

public class CommentMapper {

    public static Comment toComment(CommentDto commentDto) {
        return new Comment(commentDto.getId(), commentDto.getText(), commentDto.getUserId(),
                commentDto.getEventId(), commentDto.getCreatedOn());
    }

    public static CommentDto toCommentDto(Comment comment) {
        return new CommentDto(comment.getId(), comment.getText(), comment.getUserId(), comment.getEventId(),
                comment.getCreatedOn());
    }
}
