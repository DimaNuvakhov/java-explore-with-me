package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.dto.*;
import ru.practicum.service.interfaces.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping()
public class EventController {

    private final EventService eventService;

    // Public
    @GetMapping("/events/{eventId}")
    public EventFullDto getEventByIdPublic(@PathVariable Integer eventId, HttpServletRequest request) {
        return eventService.getEventByIdPublic(eventId, request.getRemoteAddr(), request.getRequestURI());
    }

    @GetMapping("/events")
    public Collection<EventFullDto> getPublicEvents(
            @RequestParam String text,
            @RequestParam List<Integer> categories,
            @RequestParam Boolean paid,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")  LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam Boolean onlyAvailable,
            @RequestParam String sort,
            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
            @Positive @RequestParam(defaultValue = "10") Integer size,
            HttpServletRequest request) {
        return eventService.getPublicEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort,
                from, size, request.getRemoteAddr(), request.getRequestURI());
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

    @GetMapping("/users/{userId}/events")
    public Collection<EventShortDto> getAllUsersEvents(@PathVariable Integer userId,
                                                       @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                       @Positive @RequestParam(defaultValue = "10") Integer size) {
        return eventService.getAllUsersEvents(userId, from, size);
    }

    @GetMapping("/users/{userId}/events/{eventId}")
    public EventFullDto getUserEvent(@PathVariable Integer userId, @PathVariable Integer eventId) {
        return eventService.getUserEvent(userId, eventId);
    }

    @PatchMapping("/users/{userId}/events")
    public EventFullDto updateEventByUser(@PathVariable Integer userId,
                                          @RequestBody @Valid UpdateEventRequest updateEventRequest) {
        return eventService.updateEventByUser(userId, updateEventRequest);
    }

    // Admin
    @PatchMapping("/admin/events/{eventId}/publish")
    public EventFullDto publishEvent(@PathVariable Integer eventId) {
        return eventService.publishEvent(eventId);
    }

    @PatchMapping("/admin/events/{eventId}/reject")
    public EventFullDto rejectEvent(@PathVariable Integer eventId) {
        return eventService.rejectEvent(eventId);
    }

    @PutMapping("/admin/events/{eventId}")
    public EventFullDto putEventByAdmin(@PathVariable Integer eventId,
                                        @RequestBody AdminUpdateEventRequest adminUpdateEventRequest) {
        return eventService.putEventByAdmin(eventId, adminUpdateEventRequest);
    }

    @GetMapping("/admin/events")
    public Collection<EventFullDto> getEventsByAdmin(
            @RequestParam List<Integer> users,
            @RequestParam List<String> states,
            @RequestParam List<Integer> categories,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
            @Positive @RequestParam(defaultValue = "10") Integer size
    ) {
        return eventService.getEventsByAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }
}
