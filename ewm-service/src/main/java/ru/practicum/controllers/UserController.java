package ru.practicum.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.dto.UserDto;
import ru.practicum.service.interfaces.UserService;

import java.util.Collection;

@RestController
@RequestMapping()
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("admin/users")
    public UserDto post(UserDto userDto) {
        return userService.post(userDto);
    }

    @GetMapping("admin/users")
    public Collection<UserDto> getAll(@RequestParam Integer[] ids,
                                      @RequestParam Integer from,
                                      @RequestParam Integer size) {
        return userService.getAll(ids, from, size);
    }

    @DeleteMapping("admin/users/{userId}")
    public void deleteById(@PathVariable Integer userId) {
        userService.deleteById(userId);
    }
}
