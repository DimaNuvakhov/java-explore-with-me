package ru.practicum.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.dto.EventFullDto;
import ru.practicum.model.dto.NewEventDto;
import ru.practicum.service.interfaces.EventService;

import javax.servlet.http.HttpServletRequest;
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

    // Public
    @GetMapping("/events/{eventId}")
    public EventFullDto getEventByIdPublic(@PathVariable Integer eventId, HttpServletRequest request) {
        return eventService.getEventByIdPublic(eventId, request.getRemoteAddr(), request.getRequestURI());
    }

    //Private
    @PostMapping("/users/{userId}/events")
    public EventFullDto post(@RequestBody @Valid NewEventDto newEventDto, @PathVariable Integer userId) {
        return eventService.post(newEventDto, userId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    public EventFullDto cancelEvent(@PathVariable Integer userId, @PathVariable Integer eventId) {
        return eventService.cancelEvent(userId, eventId);
    }

    // Admin
    @PatchMapping("/admin/events/{eventId}/publish")
    public EventFullDto publishEvent(@PathVariable Integer eventId) {
        return eventService.publishEvent(eventId);
    }
}
