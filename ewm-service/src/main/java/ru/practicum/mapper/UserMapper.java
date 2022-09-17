package ru.practicum.mapper;

import ru.practicum.model.User;
import ru.practicum.model.dto.UserDto;
import ru.practicum.model.dto.UserShortDto;

import java.util.ArrayList;
import java.util.List;

public class UserMapper {

    public static UserDto toUserDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }

    public static User toUser(UserDto userDto) {
        return new User(userDto.getId(), userDto.getName(), userDto.getEmail());
    }

    public static List<UserDto> toUserDtoList(List<User> users) {
        List<UserDto> userDtos = new ArrayList<>();
        for (User user : users) {
            userDtos.add(toUserDto(user));
        }
        return userDtos;
    }

    public static UserShortDto toUserShortDto(User user) {
        return new UserShortDto(user.getId(), user.getName());
    }
}
