package com.superapp.booking_service.web;

import java.util.Map;
import java.util.UUID;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.superapp.booking_service.service.WebHookService;
import com.superapp.booking_service.web.dto.WebHookDtos.PaymentCallBackReq;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/webhook")
@RequiredArgsConstructor
public class WebHookController {
    private final WebHookService webHookService;

    @PostMapping("/payment")
    public String paymentCallBack(@RequestParam Map<String, String> query, @RequestBody PaymentCallBackReq req) {
        webHookService.paymentBooking(UUID.fromString(query.get("bookingId")), query.get("partnerId"), req);
        return "success";
    }

}
