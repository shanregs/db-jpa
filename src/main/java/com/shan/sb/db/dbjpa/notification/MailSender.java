package com.shan.sb.db.dbjpa.notification;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class MailSender {
    @Async
    public void sendOrderConfirmationEmail(Long orderId) {
        System.out.println("Sending confirmation email for order ID: " + orderId);
        try {
            Thread.sleep(1000); // Simulate I/O delay
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
