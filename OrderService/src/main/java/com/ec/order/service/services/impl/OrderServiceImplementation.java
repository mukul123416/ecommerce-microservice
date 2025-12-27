package com.ec.order.service.services.impl;

import com.ec.order.service.dtos.PaymentDTO;
import com.ec.order.service.dtos.ProductDTO;
import com.ec.order.service.entities.Order;
import com.ec.order.service.exceptions.customexceptions.ResourceNotFoundException;
import com.ec.order.service.payloads.ErrorResponse;
import com.ec.order.service.payloads.SuccessResponse;
import com.ec.order.service.repo.OrderRepository;
import com.ec.order.service.services.KafKaProducerService;
import com.ec.order.service.services.OrderService;
import com.ec.order.service.utilities.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderServiceImplementation implements OrderService {
    private final OrderRepository orderRepo;
    private final ErrorResponse errorResponse;
    private final SuccessResponse successResponse;
    private final RestTemplate restTemplate;
    private final KafKaProducerService kafKaProducerService;

    public OrderServiceImplementation(OrderRepository orderRepo,
                                      ErrorResponse errorResponse,
                                      SuccessResponse successResponse,
                                      RestTemplate restTemplate,
                                      KafKaProducerService kafKaProducerService) {
        this.orderRepo = orderRepo;
        this.errorResponse = errorResponse;
        this.successResponse = successResponse;
        this.restTemplate = restTemplate;
        this.kafKaProducerService = kafKaProducerService;
    }

    @Override
    public ResponseEntity<Object> placeOrder(Order order)  {
        ResponseEntity<Object> response;
        try {
            order.setStatus("CREATED");
            Order save = this.orderRepo.save(order);
            String jsonRequestString = "{\"orderId\" : \"" + save.getId() + "\" , "
                    + "\"userId\" : \""+ save.getUserId() + "\", \"productId\" : \"" + save.getProductId() + "\",\"quantity\" : \""+save.getQuantity()+"\"}";
            this.kafKaProducerService.sendMessage(jsonRequestString);
            response = this.successResponse.responseHandler("Order placed, processing...", HttpStatus.OK,save);
        } catch (Exception ex) {
            response = this.errorResponse.responseHandler(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    @Override
    public ResponseEntity<Object> getOrderById(Long id) throws ResourceNotFoundException {
        ResponseEntity<Object> response;
        Order order = this.orderRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Order not found with id : "+id));
        response = this.successResponse.responseHandler(AppConstants.FETCHED_SUCCESSFULLY,HttpStatus.OK,order);
            return response;
    }

    @Override
    public ResponseEntity<Object> getAllOrders() {
        ResponseEntity<Object> response;
        try {
            List<Order> orders = this.orderRepo.findAll();
            response = this.successResponse.responseHandler(AppConstants.FETCHED_SUCCESSFULLY, HttpStatus.OK,orders);
        } catch (Exception ex) {
            response = this.errorResponse.responseHandler(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    @Override
    public ResponseEntity<Object> updateOrder(Long id, Order updatedOrder) throws ResourceNotFoundException {
        ResponseEntity<Object> response;
        Order order = this.orderRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Order not found with id "+id));
            order.setUserId(updatedOrder.getUserId());
            order.setProductId(updatedOrder.getProductId());
            order.setQuantity(updatedOrder.getQuantity());
            order.setStatus(updatedOrder.getStatus());
            this.orderRepo.save(order);
            response = this.successResponse.responseHandler("Update Successful",HttpStatus.OK,null);
            return response;
    }

    @Override
    public ResponseEntity<Object> deleteOrder(Long id) throws ResourceNotFoundException {
        ResponseEntity<Object> response;
        Order order = this.orderRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Order not found with id "+id));
           this.orderRepo.deleteById(order.getId());
            response = this.successResponse.responseHandler("Delete Successful", HttpStatus.OK, null);
            return response;
    }

    @Override
    public ResponseEntity<Object> getOrdersByUserId(Long userId) throws ResourceNotFoundException {
        ResponseEntity<Object> response;
        List<Order> byUserId = this.orderRepo.findByUserId(userId);


        List<Long> productIds = byUserId.stream()
                .map(Order::getProductId)
                .toList();

        String idsParam = productIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining("&ids="));

        ProductDTO[] productsArray = this.restTemplate.getForObject(
                "http://localhost:8082/products/batch?ids=" + idsParam,
                ProductDTO[].class
        );

        Map<Long, ProductDTO> productMap = Arrays.stream(productsArray)
                .collect(Collectors.toMap(ProductDTO::getId, p -> p));



        List<Long> orderIds = byUserId.stream()
                .map(Order::getId)
                .toList();

        String orderIdsParam = orderIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining("&orderIds="));

        PaymentDTO[] paymentArray = this.restTemplate.getForObject(
                "http://localhost:8084/payments/batch?orderIds=" + orderIdsParam,
                PaymentDTO[].class
        );

        Map<Long, PaymentDTO> paymentMap = Arrays.stream(paymentArray)
                .collect(Collectors.toMap(PaymentDTO::getOrderId, p -> p));



        for (Order order : byUserId) {
            order.setProduct(productMap.get(order.getProductId()));
            order.setPayment(paymentMap.get(order.getId()));
        }


        if (byUserId.isEmpty()) {
            throw new ResourceNotFoundException("Order not found with UserId "+userId);
        } else {
            response = this.successResponse.responseHandler(AppConstants.FETCHED_SUCCESSFULLY, HttpStatus.OK, byUserId);
            return response;
        }
    }

    @Override
    public ResponseEntity<Object> getOrdersByProductId(Long productId) throws ResourceNotFoundException {
        ResponseEntity<Object> response;
        List<Order> byProductId = this.orderRepo.findByProductId(productId);
        if (byProductId.isEmpty()) {
            throw new ResourceNotFoundException("Order not found with ProductId "+productId);
        } else {
            response = this.successResponse.responseHandler(AppConstants.FETCHED_SUCCESSFULLY, HttpStatus.OK, byProductId);
            return response;
        }
    }
}
