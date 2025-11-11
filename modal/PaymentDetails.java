package com.Kilari.modal;

import com.Kilari.domain.*;
import lombok.Data;

@Data
public class PaymentDetails {

    private  String  PaymentId;
    private String razorPayPaymentLinkId;
    private String getRazorPayPaymentLinkReferenceId;
    private String razorPayPaymentLinkStatus;
    private  String razorpayPaymentIdKILARI;
    private PaymentStatus status;



}
