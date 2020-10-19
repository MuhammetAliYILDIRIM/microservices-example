package com.may.accountservice.controller;

import com.may.accountservice.dto.request.UserDto;
import com.may.accountservice.dto.response.UserListDto;
import com.may.accountservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<String> createNewUser(@RequestBody UserDto userDto) {
        userService.createUser(userDto);
        return ResponseEntity.ok("Success");
    }

    @GetMapping
    public ResponseEntity<List<UserListDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("{username}")
    public ResponseEntity<UserListDto> getUserByUserId(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserByUserName(username));
    }
}
