package eprocurementapi.service;

import eprocurementapi.dao.ServiceResult;
import eprocurementapi.dao.request.OrderRequest;
import eprocurementapi.exception.UnexpectedException;

import java.util.List;

public interface OrderService {

    ServiceResult createOrder(List<OrderRequest> orders) throws UnexpectedException;

    ServiceResult getAllOrder();
}
