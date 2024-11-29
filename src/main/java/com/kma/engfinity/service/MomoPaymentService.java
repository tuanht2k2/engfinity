package com.kma.engfinity.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.kma.engfinity.DTO.request.CreatePaymentRequest;
import com.kma.engfinity.DTO.response.CommonResponse;
import com.kma.engfinity.DTO.response.CreateMomoPaymentResponse;
import com.kma.engfinity.DTO.response.MomoPaymentResultResponse;
import com.kma.engfinity.utils.Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class MomoPaymentService {
    @Value("${MOMO.ACCESS_KEY}")
    private String ACCESS_KEY;

    @Value("${MOMO.SECRET_KEY}")
    private String SECRET_KEY;

    @Value("${MOMO.PARTNER_CODE}")
    private String PARTNER_CODE;

    @Value("${NGROK.URL}")
    private String NGROK_URL;

    @Autowired
    Gson gson;

    @Autowired
    ObjectMapper mapper;

    public ResponseEntity<?> create(CreatePaymentRequest request) {
        try {
            String orderId = PARTNER_CODE + System.currentTimeMillis();
            String requestType = "payWithMethod";
            String ipnUrl = NGROK_URL;
            String redirectUrl = "https://webhook.site/b3088a6a-2d17-4f8d-a383-71389a6c600b";
            String rawSignature = "accessKey=" + ACCESS_KEY +
                    "&amount=" + request.getAmount() +
                    "&extraData=" + "Engfinity" +
                    "&ipnUrl=" + ipnUrl +
                    "&orderId=" + orderId +
                    "&orderInfo=" + request.getOrderInfo() +
                    "&partnerCode=" + PARTNER_CODE +
                    "&redirectUrl=" + redirectUrl +
                    "&requestId=" + orderId +
                    "&requestType=" + requestType;
            String signature = Encoder.signHmacSHA256(rawSignature, SECRET_KEY);

            String lang = "vi";
            boolean autoCapture = true;

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("partnerCode", PARTNER_CODE);
            requestBody.put("storeId", "MomoTestStore");
            requestBody.put("requestId", orderId);
            requestBody.put("amount", request.getAmount());
            requestBody.put("orderId", orderId);
            requestBody.put("orderInfo", request.getOrderInfo());
            requestBody.put("redirectUrl", redirectUrl);
            requestBody.put("ipnUrl", ipnUrl);
            requestBody.put("lang", lang);
            requestBody.put("requestType", requestType);
            requestBody.put("autoCapture", autoCapture);
            requestBody.put("orderGroupId", "");
            requestBody.put("signature", signature);
            requestBody.put("extraData", "Engfinity");
            String momoEndpoint = "https://test-payment.momo.vn/v2/gateway/api/create";

            String createPaymentResponse = sendHttpPost(momoEndpoint, requestBody);
            CreateMomoPaymentResponse convertedCreatePaymentResponse = mapper.readValue(createPaymentResponse, CreateMomoPaymentResponse.class);
            CommonResponse<?> response = new CommonResponse<>(200, convertedCreatePaymentResponse, "");

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    public void handlePaymentResult (MomoPaymentResultResponse response) {
        System.out.println(response.getMessage());
    }

    public String sendHttpPost(String url, Map<String, Object> requestBody) throws Exception {
        String payload = gson.toJson(requestBody);

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = payload.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int responseCode = connection.getResponseCode();
        InputStream inputStream = (responseCode >= 200 && responseCode < 300)
                ? connection.getInputStream()
                : connection.getErrorStream();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return response.toString();
        }
    }
}

