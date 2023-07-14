package eprocurementapi.mapper;

import eprocurementapi.dao.request.OrderRequest;
import eprocurementapi.entities.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL
)
public interface OrderMapper {

    @Mapping(source = "productId", target = "id.productId")
    @Mapping(source = "qty", target = "qty")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "productName", target = "productName")
    OrderItem toOrderItemEntity(OrderRequest order);

    @Mapping(source = "productId", target = "id.productId")
    @Mapping(source = "qty", target = "qty")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "productName", target = "productName")
    List<OrderItem> toOrderItemEntities(List<OrderRequest> orderItem);
}
