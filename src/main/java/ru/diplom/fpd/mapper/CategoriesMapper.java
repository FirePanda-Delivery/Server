package ru.diplom.fpd.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import ru.diplom.fpd.dto.CategoriesUpdateDto;
import ru.diplom.fpd.model.Categories;
import ru.diplom.fpd.dto.CategoriesDto;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {ProductMapper.class})
public interface CategoriesMapper {
    Categories toEntity(CategoriesDto categoriesDto);

    CategoriesDto toDto(Categories categories);


    void updateCategory(@MappingTarget Categories categories, CategoriesUpdateDto categoriesDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Categories partialUpdate(CategoriesDto categoriesDto, @MappingTarget Categories categories);
}