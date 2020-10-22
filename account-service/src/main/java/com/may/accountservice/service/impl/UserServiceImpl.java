package com.may.accountservice.service.impl;

import com.may.accountservice.dto.request.UserDto;
import com.may.accountservice.dto.response.UserListDto;
import com.may.accountservice.exception.AccountServiceException;
import com.may.accountservice.exception.ErrorType;
import com.may.accountservice.repository.UserRepository;
import com.may.accountservice.repository.entity.User;
import com.may.accountservice.service.UserService;
import com.may.accountservice.util.HashingUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.may.accountservice.exception.ErrorType.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper mapper;

    @Override
    @Transactional
    public void createUser(UserDto userDto) {

        if (userRepository.findUserByEmail(userDto.getEmail()).isPresent())
            throw new AccountServiceException(ErrorType.EMAIL_IN_USE);

        if (userRepository.findUserByUsername(userDto.getUsername()).isPresent())
            throw new AccountServiceException(ErrorType.USERNAME_IN_USE);

        User newUser = new User();
        newUser.setEmail(userDto.getEmail());
        newUser.setFullName(userDto.getFullName());
        newUser.setUsername(userDto.getUsername());
        newUser.setPassword(HashingUtil.encode(userDto.getPassword()));
        newUser.setDeleted(false);
        newUser.setActive(false);

        userRepository.save(newUser);
    }

    @Override
    public UserListDto getUserByUserName(String username) {

        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new AccountServiceException(USER_NOT_FOUND));

        return mapper.map(user, UserListDto.class);

    }

    @Override
    public List<UserListDto> getAllUsers() {

        return userRepository.findAll().stream().map(user -> mapper.map(user, UserListDto.class))
                .collect(Collectors.toList());

    }

}