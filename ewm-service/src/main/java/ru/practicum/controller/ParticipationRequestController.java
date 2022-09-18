package ru.practicum.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.dto.ParticipationRequestDto;
import ru.practicum.service.interfaces.ParticipationRequestService;

import java.util.Collection;

@Validated
@RestController
@RequestMapping()
public class ParticipationRequestController {

    private final ParticipationRequestService participationRequestService;

    @Autowired
    public ParticipationRequestController(ParticipationRequestService participationRequestService) {
        this.participationRequestService = participationRequestService;
    }

    @PostMapping("/users/{userId}/requests")
    public ParticipationRequestDto post(@PathVariable Integer userId,
                                        @RequestParam Integer eventId,
                                        ParticipationRequestDto requestDto) {
        return participationRequestService.post(userId, eventId, requestDto);
    }

    @GetMapping("/users/{userId}/requests")
    public Collection<ParticipationRequestDto> getAllUserRequests(@PathVariable Integer userId) {
        return participationRequestService.getAllUserRequests(userId);
    }

    @GetMapping("/users/{userId}/events/{eventId}/requests")
    public Collection<ParticipationRequestDto> getAllUserEventRequests(@PathVariable Integer userId,
                                                                       @PathVariable Integer eventId) {
        return participationRequestService.getAllUserEventRequests(userId, eventId);
    }


}
