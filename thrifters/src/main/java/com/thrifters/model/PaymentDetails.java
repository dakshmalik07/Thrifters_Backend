package com.thrifters.model;


import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDetails {

    private String paymentMethod;
    private String status;
    private String paymentId;
    private String razorpayPaymentLinkId;
    private String razrorpayPaymentLinkReferenceId;
    private String razrorpayPaymentLinkStatus;
    private String razrorpayPaymentId;
}
