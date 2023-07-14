package eprocurementapi.service;

import eprocurementapi.dao.ServiceResult;
import eprocurementapi.dao.request.OrderRequest;
import eprocurementapi.exception.UnexpectedException;

import java.util.List;

public interface OrderService {

    ServiceResult createOrder(String authHeader, List<OrderRequest> order) throws UnexpectedException;

    ServiceResult getAllOrder(String authHeader);

    ServiceResult getOrderDetail(int orderId) throws UnexpectedException;

    ServiceResult getAllApproval(String authHeader);

    ServiceResult cancelOrder(String authHeader, int orderId, String message);

    ServiceResult approveOrder(String authHeader, int orderId);
}
