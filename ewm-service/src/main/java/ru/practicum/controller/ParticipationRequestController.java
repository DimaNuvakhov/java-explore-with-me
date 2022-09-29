package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.dto.ParticipationRequestDto;
import ru.practicum.service.interfaces.ParticipationRequestService;

import java.util.Collection;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping()
public class ParticipationRequestController {

    private final ParticipationRequestService participationRequestService;

    @PostMapping("/users/{userId}/requests")
    public ParticipationRequestDto post(@PathVariable Integer userId,
                                        @RequestParam Integer eventId) {
        return participationRequestService.post(userId, eventId);
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

    @PatchMapping("/users/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelUserRequest(@PathVariable Integer userId, @PathVariable Integer requestId) {
        return participationRequestService.cancelUserRequest(userId, requestId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}/requests/{reqId}/confirm")
    public ParticipationRequestDto confirmRequest(@PathVariable Integer userId,
                                                  @PathVariable Integer eventId,
                                                  @PathVariable Integer reqId) {
        return participationRequestService.confirmRequest(userId, eventId, reqId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}/requests/{reqId}/reject")
    public ParticipationRequestDto rejectRequest(@PathVariable Integer userId,
                                                 @PathVariable Integer eventId,
                                                 @PathVariable Integer reqId) {
        return participationRequestService.rejectRequest(userId, eventId, reqId);
    }
}
