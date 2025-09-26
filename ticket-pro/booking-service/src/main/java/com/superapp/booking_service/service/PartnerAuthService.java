package com.superapp.booking_service.service;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Service;

import com.superapp.booking_service.domain.Partner;
import com.superapp.booking_service.repo.PartnerRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PartnerAuthService {
    private final PartnerRepo repo;

    private String computeHmac(String data, String key) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        mac.init(secretKey);
        byte[] bytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(bytes); // Convert byte array to hex string
    }

    // Helper function to convert byte array to hexadecimal string
    private String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }

    public boolean validatePartnerRequest(String partnerId, Map<String, String> fields, String checkSum) {
        Partner partner = repo.findById(partnerId).orElseThrow(() -> new IllegalStateException("Partner not found: "));
        // simple webhook request authentication
        String[] checksumFields = partner.getPartnerChecksumFormat().split("\\|");
        String dataString = java.util.Arrays.stream(checksumFields)
                .map(fields::get)
                .collect(Collectors.joining("|"));

        try {
            // Generate the HMAC from the fields and partner's secret key
            String generatedChecksum = computeHmac(dataString, partner.getPartnerSecretCode());
            return generatedChecksum.equals(checkSum);
        } catch (Exception e) {
            throw new IllegalStateException("Error while validating checksum", e);
        }
    }
}
