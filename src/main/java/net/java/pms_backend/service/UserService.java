package net.java.pms_backend.service;

import net.java.pms_backend.dto.UserDto;

public interface UserService {
    UserDto createUser(UserDto userDto);
}
