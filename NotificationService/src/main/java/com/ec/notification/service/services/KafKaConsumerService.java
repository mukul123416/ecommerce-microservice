package com.ec.notification.service.services;

import com.ec.notification.service.dtos.UserDTO;
import com.ec.notification.service.helper.UserClient;
import com.ec.notification.service.payloads.ApiResponse;
import com.ec.notification.service.payloads.OrderValidateStatusDetails;
import com.ec.notification.service.payloads.PaymentStatusDetails;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Random;

@Slf4j
@Service
public class KafKaConsumerService {

    private final Gson gson = new Gson();

    private final EmailService emailService;
    private final UserClient userClient;

    public KafKaConsumerService(EmailService emailService, UserClient userClient) {
        this.emailService = emailService;
        this.userClient = userClient;
    }

    @KafkaListener(topics = "payment-success",groupId = "${spring.kafka.consumer.group-id}")
    public void paymentSuccessConsume(String event) {
        log.info("Payment success event received: {}", event);
        try {
            PaymentStatusDetails dto = this.gson.fromJson(event, PaymentStatusDetails.class);
            UserDTO userDTO = getUserDetails(dto.getUserId());

            if (userDTO != null) {
                String subject = "Payment Confirmed for Order #" + dto.getOrderId();
                String message = buildPaymentSuccessEmail(userDTO, dto);

                this.emailService.sendEmail(subject, message, userDTO.getEmail());
                log.info("Payment success email sent to {}", userDTO.getEmail());
            }
        } catch (Exception e) {
            log.error("Error in paymentSuccessConsume: {}", e.getMessage());
        }
    }

    @KafkaListener(topics = "payment-failed", groupId = "${spring.kafka.consumer.group-id}")
    public void paymentFailedConsume(String event) {
        log.error("Payment failure notification trigger: {}", event);
        try {
            PaymentStatusDetails dto = this.gson.fromJson(event, PaymentStatusDetails.class);
            UserDTO userDTO = getUserDetails(dto.getUserId());

            if (userDTO != null) {
                String subject = "Action Required: Payment Failed for Order #" + dto.getOrderId();
                String message = buildPaymentFailedEmail(userDTO, dto);

                this.emailService.sendEmail(subject, message, userDTO.getEmail());
                log.info("Payment failure email sent to {}", userDTO.getEmail());
            }
        } catch (Exception e) {
            log.error("Failed to process payment-failed event: {}", e.getMessage());
        }
    }

    private String buildPaymentFailedEmail(UserDTO userDTO, PaymentStatusDetails dto) {

        String retryPaymentUrl = "https://yourstore.com/checkout/" + dto.getOrderId(); // Retry link
        String supportUrl = "https://yourstore.com/support";

        return "<div style='font-family: \"Segoe UI\", Roboto, sans-serif; max-width: 600px; margin: auto; border: 1px solid #ddd; border-radius: 12px; overflow: hidden;'>"
                + "  <div style='background: linear-gradient(135deg, #dc3545 0%, #c82333 100%); color: white; padding: 40px 20px; text-align: center;'>"
                + "    <div style='background: rgba(255,255,255,0.2); width: 60px; height: 60px; line-height: 60px; border-radius: 50%; margin: 0 auto 15px; font-size: 30px;'>!</div>"
                + "    <h1 style='margin: 0; font-size: 26px;'>Payment Failed</h1>"
                + "    <p style='margin: 10px 0 0; opacity: 0.9;'>Order #" + dto.getOrderId() + " could not be processed</p>"
                + "  </div>"
                + "  <div style='padding: 30px; color: #333; line-height: 1.6;'>"
                + "    <p>Hi <b>" + userDTO.getName() + "</b>,</p>"
                + "    <p>We're sorry, but the payment for your order has failed. Don't worry, if any amount was deducted, it will be refunded within 5-7 business days.</p>"

                // --- Retry Button ---
                + "    <div style='text-align: center; margin: 30px 0;'>"
                + "      <a href='" + retryPaymentUrl + "' style='background-color: #dc3545; color: white; padding: 14px 30px; text-decoration: none; border-radius: 6px; font-weight: bold; display: inline-block;'>Retry Payment</a>"
                + "    </div>"

                + "    <div style='background-color: #fff5f5; padding: 20px; border-radius: 8px; border: 1px solid #feb2b2;'>"
                + "      <h4 style='margin: 0 0 10px 0; color: #c53030;'>Possible Reasons:</h4>"
                + "      <ul style='margin: 0; padding-left: 20px; font-size: 14px; color: #742a2a;'>"
                + "        <li>Incorrect card details or expired card.</li>"
                + "        <li>Insufficient funds in the account.</li>"
                + "        <li>Transaction declined by your bank.</li>"
                + "      </ul>"
                + "    </div>"

                + "    <p style='margin-top: 25px; text-align: center; font-size: 14px;'>"
                + "      Need help? <a href='" + supportUrl + "' style='color: #007bff; text-decoration: none;'>Contact Support Team</a>"
                + "    </p>"
                + "  </div>"
                + "  <div style='background-color: #f1f1f1; padding: 20px; text-align: center; font-size: 12px; color: #888; border-top: 1px solid #eee;'>"
                + "    <p style='margin: 0;'>Sent by <b>SCM Store</b></p>"
                + "  </div>"
                + "</div>";
    }

    private String buildPaymentSuccessEmail(UserDTO userDTO, PaymentStatusDetails dto) {

        String formattedAmount = new java.text.DecimalFormat("##,##,###.00").format(dto.getAmount());

        return "<div style='font-family: \"Segoe UI\", Tahoma, Geneva, Verdana, sans-serif; max-width: 600px; margin: auto; border: 1px solid #ddd; border-radius: 10px; overflow: hidden;'>"
                + "  <div style='background-color: #28a745; color: white; padding: 30px; text-align: center;'>"
                + "    <div style='font-size: 50px; margin-bottom: 10px;'>&#10004;</div>" // Large Checkmark
                + "    <h1 style='margin: 0; font-size: 24px;'>Payment Successful!</h1>"
                + "    <p style='margin: 5px 0 0;'>Thank you for your payment.</p>"
                + "  </div>"
                + "  <div style='padding: 30px; color: #444;'>"
                + "    <p style='font-size: 16px;'>Hi <b>" + userDTO.getName() + "</b>,</p>"
                + "    <p>We've received your payment for <b>Order #" + dto.getOrderId() + "</b>. Your order is now being processed and will be shipped soon.</p>"
                + "    <hr style='border: 0; border-top: 1px solid #eee; margin: 20px 0;'>"
                + "    <table style='width: 100%; font-size: 15px;'>"
                + "      <tr>"
                + "        <td style='padding: 5px 0; color: #777;'>Payment Status:</td>"
                + "        <td style='padding: 5px 0; text-align: right; color: #28a745;'><b>" + dto.getStatus().toUpperCase() + "</b></td>"
                + "      </tr>"
                + "      <tr>"
                + "        <td style='padding: 5px 0; color: #777;'>Amount Paid:</td>"
                + "        <td style='padding: 5px 0; text-align: right; font-size: 18px;'><b>&#8377; " + formattedAmount + "</b></td>"
                + "      </tr>"
                + "      <tr>"
                + "        <td style='padding: 5px 0; color: #777;'>Quantity:</td>"
                + "        <td style='padding: 5px 0; text-align: right;'>" + dto.getQuantity() + "</td>"
                + "      </tr>"
                + "    </table>"
                + "    <div style='margin-top: 30px; padding: 20px; background-color: #f8f9fa; border-radius: 5px; border-left: 4px solid #28a745;'>"
                + "      <p style='margin: 0; font-size: 14px; color: #555;'>"
                + "        <b>Next Steps:</b> You will receive another email with tracking details once your order is dispatched."
                + "      </p>"
                + "    </div>"
                + "    <div style='text-align: center; margin-top: 30px;'>"
                + "      <a href='#' style='background-color: #28a745; color: white; padding: 12px 25px; text-decoration: none; border-radius: 5px; font-weight: bold;'>View Order History</a>"
                + "    </div>"
                + "  </div>"
                + "  <div style='background-color: #f1f1f1; padding: 15px; text-align: center; font-size: 12px; color: #888;'>"
                + "    If you have any issues with this transaction, please reply to this email.<br>"
                + "    &copy; 2025 SCM Store. All rights reserved."
                + "  </div>"
                + "</div>";
    }

    @KafkaListener(topics = "order-validated",groupId = "${spring.kafka.consumer.group-id}")
    public void orderValidatedConsume(String event) {

        log.info("Inventory Check Success. Processing Notification for Event: {}", event);

        try {

        OrderValidateStatusDetails dto = this.gson.fromJson(event, OrderValidateStatusDetails.class);

        UserDTO userDTO = getUserDetails(dto.getUserId());

        if (userDTO != null && userDTO.getEmail() != null) {

            String otp = new DecimalFormat("000000").format(new Random().nextInt(999999));

            String subject = "Order Placed Successfully! - Order #" + dto.getOrderId();

            String message = "<div style='font-family: Arial, sans-serif; max-width: 600px; border: 1px solid #eee; padding: 20px; color: #333;'>"
                    + "<div style='text-align: center; border-bottom: 2px solid #4CAF50; padding-bottom: 10px;'>"
                    + "  <h1 style='color: #4CAF50; margin: 0;'>Order Confirmed!</h1>"
                    + "</div>"
                    + "<div style='padding: 20px 0;'>"
                    + "  <p>Hi <b>" + userDTO.getName() + "</b>,</p>"
                    + "  <p>Great news! Your order has been successfully placed. Our inventory check is complete, and your items are being prepared for shipping.</p>"
                    + "  <div style='background-color: #f9f9f9; padding: 15px; border-radius: 8px; margin: 20px 0;'>"
                    + "    <h3 style='margin-top: 0; color: #555;'>Order Summary</h3>"
                    + "    <table style='width: 100%; border-collapse: collapse;'>"
                    + "      <tr><td>Order ID:</td><td style='text-align: right;'><b>#" + dto.getOrderId() + "</b></td></tr>"
                    + "      <tr><td>Product ID:</td><td style='text-align: right;'>#" + dto.getProductId() + "</td></tr>"
                    + "      <tr><td>Quantity:</td><td style='text-align: right;'>" + dto.getQuantity() + "</td></tr>"
                    + "      <tr><td style='padding-top: 10px; font-size: 18px;'><b>Total Amount:</b></td>"
                    + "          <td style='padding-top: 10px; text-align: right; font-size: 18px; color: #4CAF50;'><b>Rs." + (dto.getPrice() * dto.getQuantity()) + "</b></td></tr>"
                    + "    </table>"
                    + "  </div>"
                    + "  <div style='text-align: center; margin: 30px 0;'>"
                    + "    <p style='margin-bottom: 10px; color: #777;'>Use this OTP for order tracking security:</p>"
                    + "    <span style='background: #e8f5e9; padding: 10px 25px; font-size: 24px; font-weight: bold; color: #2e7d32; border: 1px dashed #2e7d32;'>" + otp + "</span>"
                    + "  </div>"
                    + "  <p style='font-size: 12px; color: #999; line-height: 1.5;'>"
                    + "    If you didn't request this or have questions, please contact our support team.<br>"
                    + "    Thank you for shopping with <b>SCM Store</b>!"
                    + "  </p>"
                    + "</div>"
                    + "</div>";
        String to = userDTO.getEmail();
        boolean isSent = this.emailService.sendEmail(subject, message, to);
            if(isSent) {
                log.info("Professional Order Confirmation sent to: {}", userDTO.getEmail());
            }
        } else {
            log.error("Notification Failed: User details not found for ID {}", dto.getUserId());
        }

    } catch (Exception e) {
            log.error("Error processing inventory-validated event: {}", e.getMessage());
    }
    }

    private UserDTO getUserDetails(Long userId) {
        log.debug("Fetching user details from UserClient for ID: {}", userId);
        try {
            ApiResponse<UserDTO> apiResponse = this.userClient.fetchUserBasic(userId);
            if (apiResponse != null && apiResponse.getData() != null) {
                return apiResponse.getData();
            }
        } catch (Exception e) {
            log.error("Failed to fetch user details for ID {}: {}", userId, e.getMessage());
        }
        return null;
    }
}

