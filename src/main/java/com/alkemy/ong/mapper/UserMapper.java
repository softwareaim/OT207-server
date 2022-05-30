package com.alkemy.ong.mapper;

import com.alkemy.ong.dto.UserDto;
import com.alkemy.ong.auth.dto.UserResponseDto;
import com.alkemy.ong.model.User;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
public class UserMapper {

    public User UserDto2Entity(@NotNull UserDto dto){
        User entity = new User();
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setEmail(dto.getEmail());
        entity.setPassword(dto.getPassword());
        entity.setRole(dto.getRole());
        return entity;
    }

    public UserDto UserEntity2Dto(@NotNull User entity){
        UserDto dto = new UserDto();
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setEmail(entity.getEmail());
        dto.setPassword(entity.getPassword());
        dto.setRole(entity.getRole());
        return dto;
    }

    public UserResponseDto UserEntity2ResponseDto(@NotNull User entity){
        UserResponseDto dto = new UserResponseDto();
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setEmail(entity.getEmail());
        dto.setRoleId(entity.getRole());
        return dto;
    }

}
