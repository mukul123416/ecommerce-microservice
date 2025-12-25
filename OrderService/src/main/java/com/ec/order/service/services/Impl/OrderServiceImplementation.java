package com.ec.order.service.services.Impl;

import com.ec.order.service.dtos.PaymentDTO;
import com.ec.order.service.dtos.ProductDTO;
import com.ec.order.service.entities.Order;
import com.ec.order.service.exceptions.customexceptions.ResourceNotFoundException;
import com.ec.order.service.payloads.ErrorResponse;
import com.ec.order.service.payloads.SuccessResponse;
import com.ec.order.service.repo.OrderRepository;
import com.ec.order.service.services.KafKaProducerService;
import com.ec.order.service.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private OrderRepository orderRepo;
    @Autowired
    private ErrorResponse errorResponse;
    @Autowired
    private SuccessResponse successResponse;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private KafKaProducerService kafKaProducerService;

    @Override
    public ResponseEntity<?> placeOrder(Order order)  {
        ResponseEntity<?> response;
        try {
            order.setStatus("CREATED");
            Order save = orderRepo.save(order);
            String jsonRequestString = "{\"orderId\" : \"" + save.getId() + "\" , "
                    + "\"userId\" : \""+ save.getUserId() + "\", \"productId\" : \"" + save.getProductId() + "\",\"quantity\" : \""+save.getQuantity()+"\"}";
            kafKaProducerService.sendMessage(jsonRequestString);
            response = successResponse.responseHandler("Order placed, processing...", HttpStatus.OK,save);
        } catch (Exception ex) {
            response = errorResponse.responseHandler(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    @Override
    public ResponseEntity<?> getOrderById(Long id) throws ResourceNotFoundException {
        ResponseEntity<?> response;
        Order order = orderRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Order not found with id : "+id));
        response = successResponse.responseHandler("Fetched Successful",HttpStatus.OK,order);
            return response;
    }

    @Override
    public ResponseEntity<?> getAllOrders() {
        ResponseEntity<?> response;
        try {
            List<Order> orders = orderRepo.findAll();
            response = successResponse.responseHandler("Fetched Successful", HttpStatus.OK,orders);
        } catch (Exception ex) {
            response = errorResponse.responseHandler(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    @Override
    public ResponseEntity<?> updateOrder(Long id, Order updatedOrder) throws ResourceNotFoundException {
        ResponseEntity<?> response;
        Order order = orderRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Order not found with id "+id));
            order.setUserId(updatedOrder.getUserId());
            order.setProductId(updatedOrder.getProductId());
            order.setQuantity(updatedOrder.getQuantity());
            order.setStatus(updatedOrder.getStatus());
            orderRepo.save(order);
            response = successResponse.responseHandler("Update Successful",HttpStatus.OK,null);
            return response;
    }

    @Override
    public ResponseEntity<?> deleteOrder(Long id) throws ResourceNotFoundException {
        ResponseEntity<?> response;
        Order order = orderRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Order not found with id "+id));
            orderRepo.deleteById(order.getId());
            response = successResponse.responseHandler("Delete Successful", HttpStatus.OK, null);
            return response;
    }

    @Override
    public ResponseEntity<?> getOrdersByUserId(Long userId) throws ResourceNotFoundException {
        ResponseEntity<?> response;
        List<Order> byUserId = orderRepo.findByUserId(userId);


        List<Long> productIds = byUserId.stream()
                .map(Order::getProductId)
                .toList();

        String idsParam = productIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining("&ids="));

        ProductDTO[] productsArray = restTemplate.getForObject(
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

        PaymentDTO[] paymentArray = restTemplate.getForObject(
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
            response = successResponse.responseHandler("Fetched Successful", HttpStatus.OK, byUserId);
            return response;
        }
    }

    @Override
    public ResponseEntity<?> getOrdersByProductId(Long productId) throws ResourceNotFoundException {
        ResponseEntity<?> response;
        List<Order> byProductId = orderRepo.findByProductId(productId);
        if (byProductId.isEmpty()) {
            throw new ResourceNotFoundException("Order not found with ProductId "+productId);
        } else {
            response = successResponse.responseHandler("Fetched Successful", HttpStatus.OK, byProductId);
            return response;
        }
    }
}
