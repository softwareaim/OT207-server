package com.alkemy.ong.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ContactDTO {

    private Long id;

    @NotNull(message = "Name cannot be empty")
    private String name;

    @NotBlank(message = "Phone cannot be empty")
    private String phone;

    @Email(message = "Has to be an email format")
    @NotBlank
    private String email;

    @NotNull(message = "Name cannot be empty")
    private String message;
}
