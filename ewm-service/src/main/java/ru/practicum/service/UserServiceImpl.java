package ru.practicum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.mappers.UserMapper;
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
    public List<UserDto> getAll(Integer[] ids, Integer from, Integer size) {
        List<UserDto> userDtos = new ArrayList<>();
        if (ids != null) {
            for (Integer id : ids) {
                User newUser = userRepository.findById(id).orElse(new User());
                if (newUser.getId() != null) {
                    userDtos.add(UserMapper.toUserDto(newUser)); // TODO Разобраться
                }
            }
            return userDtos;
        } else {
            PageRequest pageRequest = PageRequest.of(from / size, size, Sort.by("id"));
            return userRepository.findAll(pageRequest).stream()
                    .map(UserMapper::toUserDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public void deleteById(Integer userId) {
        userRepository.deleteById(userId);
    }
}
