package com.alkemy.ong.dto;

import com.alkemy.ong.model.News;
import com.alkemy.ong.model.UserEntity;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CommentaryDTO {

    private Long id;

    @NotBlank(message = "Commentary cannot be null")
    private String body;

    @NotNull(message = "User id cannot be null")
    private UserEntity userEntity;

    @NotNull(message = "News id cannot be null")
    private News news;
}
