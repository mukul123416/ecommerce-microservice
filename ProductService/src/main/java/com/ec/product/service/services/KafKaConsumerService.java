package com.ec.product.service.services;
import com.ec.product.service.entities.Product;
import com.ec.product.service.exceptions.customexceptions.ResourceNotFoundException;
import com.ec.product.service.payloads.PlaceOrderDetails;
import com.ec.product.service.repo.ProductRepository;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import java.util.concurrent.ExecutionException;

@Service
public class KafKaConsumerService {

    private static final Logger logger =
            LoggerFactory.getLogger(KafKaConsumerService.class);

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private KafKaProducerService kafKaProducerService;

    @KafkaListener(topics = "order-created",groupId = "${spring.kafka.consumer.group-id}")
    public void consume(String event) throws ResourceNotFoundException, ExecutionException, InterruptedException {
        PlaceOrderDetails placeOrderDetails;
        Gson gson = new Gson();
        placeOrderDetails=gson.fromJson(event, PlaceOrderDetails.class);
        Long id = placeOrderDetails.getProductId();

        Product product = productRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Product not found with id : "+id));

        String jsonRequestString = "{"
                + "\"userId\" : \"" + placeOrderDetails.getUserId() + "\" , "
                + "\"orderId\" : \"" + placeOrderDetails.getOrderId() + "\", "
                + "\"price\" : \"" + product.getPrice() + "\", "
                + "\"productId\" : \"" + placeOrderDetails.getProductId() + "\", "
                + "\"quantity\" : \"" + placeOrderDetails.getQuantity() + "\""
                + "}";

        this.kafKaProducerService.sendMessage(jsonRequestString);

    }
}

