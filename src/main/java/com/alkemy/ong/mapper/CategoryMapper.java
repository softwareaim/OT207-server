package com.alkemy.ong.mapper;

import com.alkemy.ong.auth.dto.UserResponseDto;
import com.alkemy.ong.dto.CategoryDTO;
import com.alkemy.ong.dto.CategoryDtoName;
import com.alkemy.ong.model.Category;
import com.alkemy.ong.model.UserEntity;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Component
public class CategoryMapper {

    public Category categoryDtoToCategoryEntity(CategoryDTO categoryDto){
        Category categoryEntity = new Category();
        categoryEntity.setName(categoryDto.getName());
        categoryEntity.setDescription(categoryDto.getDescription());
        categoryEntity.setImage(categoryDto.getImage());
        return categoryEntity;
    }
    public CategoryDTO categoryEntityToCategoryDto(Category savedEntity) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName(savedEntity.getName());
        categoryDTO.setDescription(savedEntity.getDescription());
        categoryDTO.setImage(savedEntity.getImage());
        return categoryDTO;
    }
    public CategoryDtoName categoryResponseDto(@NotNull Category category) {
        CategoryDtoName dto = new CategoryDtoName();
        dto.setName(category.getName());
        return dto;
    }

    public List<CategoryDtoName> CategoryListResponseDtoList(@NotEmpty List<Category> categories) {
        List<CategoryDtoName> responseDTos = new ArrayList<>();
        for (Category category : categories) {
            responseDTos.add(this.categoryResponseDto(category));
        }
        return responseDTos;
    }
}
