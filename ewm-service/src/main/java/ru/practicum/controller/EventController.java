package ru.practicum.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.dto.EventFullDto;
import ru.practicum.model.dto.NewEventDto;
import ru.practicum.service.interfaces.EventService;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping()
public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("/users/{userId}/events")
    public EventFullDto post(@RequestBody @Valid NewEventDto newEventDto, @PathVariable Integer userId) {
        return eventService.post(newEventDto, userId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    public EventFullDto cancelEvent(@PathVariable Integer userId, @PathVariable Integer eventId) {
        return eventService.cancelEvent(userId, eventId);
    }


}
