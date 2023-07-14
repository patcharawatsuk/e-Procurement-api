package eprocurementapi.service.impl;

import eprocurementapi.entities.AppovalOrderInfo;
import eprocurementapi.dao.ServiceResult;
import eprocurementapi.dao.request.OrderRequest;
import eprocurementapi.entities.*;
import eprocurementapi.exception.UnexpectedException;
import eprocurementapi.mapper.OrderMapper;
import eprocurementapi.repository.*;
import eprocurementapi.repository.custom.OrderRepositoryCustom;
import eprocurementapi.service.JwtService;
import eprocurementapi.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepo;
    private final OrderItemRepository orderItemRepo;
    private final OrderApprovalRepository orderApprovalRepo;
    private final ApproverRepository approverRepo;
    private final JwtService jwtService;
    private final UserRepository userRepo;
    private final OrderRepositoryCustom orderRepoCustom;

    private final OrderMapper orderMapper = Mappers.getMapper(OrderMapper.class);

    @Override
    @Transactional
    public ServiceResult createOrder(String authHeader, List<OrderRequest> request) throws UnexpectedException {
        String jwt = authHeader.substring(7);
        String email = jwtService.extractUserName(jwt);

        Order order = new Order();
        order.setRequester(email);
        List<OrderItem> orderItems = orderMapper.toOrderItemEntities(request);

        try {
            Order orderSave = orderRepo.save(order);

            List<OrderItem> _orderItems = orderItems.stream().map(e -> {
                int productId = e.getId().getProductId();

                OrderItemPK itemPK = new OrderItemPK();
                itemPK.setOrderId(orderSave.getId());
                itemPK.setProductId(productId);

                e.setId(itemPK);
                return e;
            }).collect(Collectors.toList());
            orderItemRepo.saveAll(_orderItems);

            //approval
            double sum = _orderItems.stream().mapToDouble(item -> item.getPrice() * item.getQty()).sum();
            if (sum <= 50000) {
                createApprovalLine(1, orderSave, ApproverLevel.LEVEL1, 1);
            } else if (sum > 50000 && sum < 200000) {
                //level2
                createApprovalLine(1, orderSave, ApproverLevel.LEVEL1, 1);
                createApprovalLine(2, orderSave, ApproverLevel.LEVEL2, 0);
            } else {
                //level3
                createApprovalLine(1, orderSave, ApproverLevel.LEVEL1, 1);
                createApprovalLine(2, orderSave, ApproverLevel.LEVEL2, 0);
                createApprovalLine(3, orderSave, ApproverLevel.LEVEL3, 0);
            }

            //purchaser
            Optional<User> purOpt = userRepo.findByRole(Role.PURCHASER);
            if (purOpt.isEmpty()) {
                throw UnexpectedException.handleCustomError("no purchaser");
            }
            int lastApproval = orderApprovalRepo.findLastApproveStep(orderSave.getId());
            int purStep = lastApproval + 1;
            OrderApprovalPK orderApprovalPK = new OrderApprovalPK();
            orderApprovalPK.setOrderId(orderSave.getId());
            orderApprovalPK.setStep(purStep);
            OrderApproval orderApproval = new OrderApproval();
            orderApproval.setApprover(purOpt.get().getEmail());
            orderApproval.setCurrent(0);
            orderApproval.setId(orderApprovalPK);
            orderApprovalRepo.save(orderApproval);


        } catch (Exception ex) {
            throw UnexpectedException.databaseFailHandling("save data fail: " + ex.getMessage());
        }
        ServiceResult serviceResult = new ServiceResult();
        serviceResult.setStatus(HttpStatus.CREATED.value());
        serviceResult.createResponseData("order created");
        return serviceResult;
    }

    @Transactional
    public void createApprovalLine(int step, Order orderSave, ApproverLevel level, int current) throws UnexpectedException {
        OrderApprovalPK orderApprovalPK = new OrderApprovalPK();
        orderApprovalPK.setOrderId(orderSave.getId());
        orderApprovalPK.setStep(step);

        Optional<Approver> appOpt = approverRepo.findByLevel(level);
        if (appOpt.isEmpty()) {
            throw UnexpectedException.handleCustomError("no approver");
        }
        OrderApproval orderApproval = new OrderApproval();
        orderApproval.setApprover(appOpt.get().getEmail());
        orderApproval.setCurrent(current);
        orderApproval.setId(orderApprovalPK);
        orderApprovalRepo.save(orderApproval);
    }

    @Override
    public ServiceResult getAllOrder(String authHeader) {
        String jwt = authHeader.substring(7);
        String email = jwtService.extractUserName(jwt);
        List<Order> allOrder = orderRepo.findByRequesterOrderByIdAsc(email);
        ServiceResult serviceResult = new ServiceResult();
        serviceResult.createResponseData(allOrder);
        return serviceResult;
    }

    @Override
    public ServiceResult getAllApproval(String authHeader) {
        String jwt = authHeader.substring(7);
        String email = jwtService.extractUserName(jwt);
        List<OrderApproval> orderApproval = orderApprovalRepo.findByApproverAndCurrentEquals(email, 1);
        List<Integer> ordersId = orderApproval.stream().map(e -> e.getId().getOrderId()).collect(Collectors.toList());
        List<AppovalOrderInfo> appovalOrderInfo = orderRepoCustom.findAppovalOrderInfo(ordersId, email);
        ServiceResult serviceResult = new ServiceResult();
        serviceResult.createResponseData(appovalOrderInfo);
        return serviceResult;
    }

    @Override
    @Transactional
    public ServiceResult approveOrder(String authHeader, int orderId) {
        String jwt = authHeader.substring(7);
        String email = jwtService.extractUserName(jwt);
        OrderApproval _approval = orderApprovalRepo.findByApproverAndIdOrderIdAndCurrentEquals(email, orderId, 1);
        _approval.setCurrent(9);


        Optional<OrderApproval> _nextApprovalOpt = orderApprovalRepo.findByIdOrderIdAndIdStep(orderId, _approval.getId().getStep() + 1);
        if (_nextApprovalOpt.isPresent()) {
            OrderApproval _nextApproval = _nextApprovalOpt.get();
            _nextApproval.setCurrent(1);
            orderApprovalRepo.save(_nextApproval);
        } else {
            Order order = orderRepo.findById(orderId).get();
            order.setCancel(-9);
            order.setComment("order is approved successfully");
        }
        orderApprovalRepo.save(_approval);
        ServiceResult serviceResult = new ServiceResult();
        serviceResult.createResponseData("approved order");
        return serviceResult;
    }
    @Override
    @Transactional
    public ServiceResult cancelOrder(String authHeader, int orderId, String message) {
        String jwt = authHeader.substring(7);
        String email = jwtService.extractUserName(jwt);
        List<OrderApproval> byIdOrderId = orderApprovalRepo.findByIdOrderId(orderId);
        List<OrderApproval> updateOrderStatus = byIdOrderId.stream().map(e -> {
            e.setCurrent(-9);
            return e;
        }).collect(Collectors.toList());
        orderApprovalRepo.saveAll(updateOrderStatus);
        ServiceResult serviceResult = new ServiceResult();
        serviceResult.createResponseData("order canceled");

        Order order = orderRepo.findById(orderId).get();
        order.setComment(String.format("Calceled By: %s", email));
        order.setCancel(1);
        orderRepo.save(order);
        return serviceResult;
    }

    @Override
    public ServiceResult getOrderDetail(int orderId) throws UnexpectedException {
        //check no hack injection
//        String jwt = authHeader.substring(7);
//        String email = jwtService.extractUserName(jwt);
//        Optional<Order> orderOpt = orderRepo.findByRequesterAndId(email, orderId);
//        if (orderOpt.isEmpty()) {
//            throw UnexpectedException.handleCustomError("order not found");
//        }

        List<OrderItem> orderItem = orderItemRepo.findByIdOrderId(orderId);
        ServiceResult serviceResult = new ServiceResult();
        serviceResult.createResponseData(orderItem);
        return serviceResult;
    }
}
