package ru.practicum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.exception.UserNotFoundException;
import ru.practicum.mapper.UserMapper;
import ru.practicum.model.User;
import ru.practicum.model.dto.UserDto;
import ru.practicum.repository.UserRepository;
import ru.practicum.service.interfaces.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto post(UserDto userDto) {
        return UserMapper.toUserDto(userRepository.save(UserMapper.toUser(userDto)));
    }

    @Override
    public List<UserDto> getAll(List<Integer> ids, Integer from, Integer size) {
        if (ids != null) {
            List<User> users = userRepository.findUsers(ids);
            return UserMapper.toUserDtoList(users);
        } else {
            PageRequest pageRequest = PageRequest.of(from / size, size, Sort.by("id"));
            return userRepository.findAll(pageRequest).stream()
                    .map(UserMapper::toUserDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public void deleteById(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User with id " + userId + " was not found.");
        }
        userRepository.deleteById(userId);
    }
}
