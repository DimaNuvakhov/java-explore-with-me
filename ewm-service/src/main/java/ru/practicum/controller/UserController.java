package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.dto.UserDto;
import ru.practicum.service.interfaces.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping()
public class UserController {

    private final UserService userService;

    @PostMapping("/admin/users")
    public UserDto post(@RequestBody @Valid UserDto userDto) {
        return userService.post(userDto);
    }

    @GetMapping("/admin/users")
    public Collection<UserDto> getAll(@RequestParam(required = false) List<Integer> ids,
                                      @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                      @Positive @RequestParam(defaultValue = "10") Integer size) {
        return userService.getAll(ids, from, size);
    }

    @DeleteMapping("/admin/users/{userId}")
    public void deleteById(@PathVariable Integer userId) {
        userService.deleteById(userId);
    }
}
