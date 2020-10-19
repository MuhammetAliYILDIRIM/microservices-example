package com.may.accountservice.service;

import com.may.accountservice.dto.request.UserDto;
import com.may.accountservice.dto.response.UserListDto;

import java.util.List;

public interface UserService {

    void createUser(UserDto userDto);

    UserListDto getUserByUserName(String username);

    List<UserListDto> getAllUsers();
}
