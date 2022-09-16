package ru.practicum.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.dto.UserDto;
import ru.practicum.service.interfaces.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@Validated
@RestController
@RequestMapping()
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("admin/users")
    public UserDto post(@RequestBody @Valid UserDto userDto) {
        return userService.post(userDto);
    }

    @GetMapping("admin/users")
    public Collection<UserDto> getAll(@RequestParam Integer[] ids,
                                      @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                      @Positive @RequestParam(defaultValue = "10") Integer size) {
        return userService.getAll(ids, from, size);
    }

    @DeleteMapping("admin/users/{userId}")
    public void deleteById(@PathVariable Integer userId) {
        userService.deleteById(userId);
    }
}
