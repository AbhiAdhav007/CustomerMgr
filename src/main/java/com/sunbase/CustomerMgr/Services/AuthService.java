package com.sunbase.CustomerMgr.Services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
@Service
public class AuthService {

    @Value("${auth.url}")
    private String authUrl;

    @Autowired
    RestTemplate restTemplate;
    private Logger logger = LoggerFactory.getLogger(AuthService.class);

    public String authenticate() {
        Map<String, String> body = new HashMap<>();
        body.put("login_id", "test@sunbasedata.com");
        body.put("password", "Test@123");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);

        Map<String, Object> response = restTemplate.exchange(authUrl, HttpMethod.POST, entity, Map.class).getBody();
        logger.info("frd:{}", response.get("access_token"));
        return (String) response.get("access_token");
    }

}
