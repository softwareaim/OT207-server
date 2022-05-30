package com.alkemy.ong.auth.service;

import com.alkemy.ong.dto.UserDto;
import com.alkemy.ong.auth.dto.UserResponseDto;

public interface IUserService {

    UserResponseDto register(UserDto userDto);

}
