package ru.diplom.fpd.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import ru.diplom.fpd.dto.OrderProductDto;
import ru.diplom.fpd.dto.ProductDto;
import ru.diplom.fpd.model.OrderProduct;
import ru.diplom.fpd.model.Product;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductMapper {

    OrderProduct toOrderEntity(OrderProductDto orderProductDto);

    OrderProductDto toOrderDto(OrderProduct orderProduct);

    Product toEntity(ProductDto productDto);

    ProductDto toDto(Product product);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Product partialUpdate(ProductDto productDto, @MappingTarget Product product);

    void updateProduct(@MappingTarget Product product, ProductDto productdto);
}