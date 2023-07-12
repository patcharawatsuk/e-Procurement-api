package eprocurementapi.mapper;

import eprocurementapi.dao.request.OrderRequest;
import eprocurementapi.entities.Order;
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

    @Mapping(source = "productId", target = "productId")
    @Mapping(source = "qty", target = "qty")
    @Mapping(source = "price", target = "price")
    Order toOrderEntity(OrderRequest order);

    @Mapping(source = "productId", target = "productId")
    @Mapping(source = "qty", target = "qty")
    @Mapping(source = "price", target = "price")
    List<Order> toOrderEntities(List<OrderRequest> order);
}
