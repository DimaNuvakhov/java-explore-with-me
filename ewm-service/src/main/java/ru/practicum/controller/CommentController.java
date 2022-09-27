package ru.practicum.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.dto.CommentDto;
import ru.practicum.service.interfaces.CommentService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@Validated
@RestController
@RequestMapping()
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/users/{userId}/events/{eventId}/comment")
    public CommentDto post(@PathVariable Integer userId,
                           @PathVariable Integer eventId,
                           @RequestBody CommentDto commentDto) {
        return commentService.post(userId, eventId, commentDto);
    }

    @GetMapping("/events/{eventId}/comment")
    public Collection<CommentDto> getEventComments(@PathVariable Integer eventId,
                                                   @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                   @Positive @RequestParam(defaultValue = "10") Integer size) {
        return commentService.getEventComments(eventId, from, size);
    }

    @PatchMapping("/comment")
    public CommentDto patch(@RequestBody CommentDto commentDto) {
        return commentService.patch(commentDto);
    }
}
