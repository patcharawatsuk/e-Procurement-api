package eprocurementapi.service.impl;

import eprocurementapi.dao.ServiceResult;
import eprocurementapi.dao.request.OrderRequest;
import eprocurementapi.entities.Order;
import eprocurementapi.exception.UnexpectedException;
import eprocurementapi.mapper.OrderMapper;
import eprocurementapi.repository.OrderRepository;
import eprocurementapi.service.OrderService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepo;

    private final OrderMapper orderMapper = Mappers.getMapper(OrderMapper.class);

    @Override
    public ServiceResult createOrder(List<OrderRequest> orders) throws UnexpectedException {
        List<Order> orderEntities = orderMapper.toOrderEntities(orders);
        try {
            orderRepo.saveAll(orderEntities);
        } catch (Exception ex) {
            throw UnexpectedException.databaseFailHandling("save data fail: " + ex.getMessage());
        }
        ServiceResult serviceResult = new ServiceResult();
        serviceResult.createResponseData("order created");
        return serviceResult;
    }

    @Override
    public ServiceResult getAllOrder() {
        List<Order> allOrder = orderRepo.findAll();
        ServiceResult serviceResult = new ServiceResult();
        serviceResult.createResponseData(allOrder);
        return serviceResult;
    }
}
