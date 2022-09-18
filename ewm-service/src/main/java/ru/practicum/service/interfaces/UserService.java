package ru.practicum.service.interfaces;

import ru.practicum.model.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto post(UserDto userDto);

    List<UserDto> getAll(List<Integer> ids, Integer from, Integer size);

    void deleteById(Integer userId);

}
