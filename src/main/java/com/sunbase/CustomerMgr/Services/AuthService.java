package com.sunbase.CustomerMgr.Services;

import com.sunbase.CustomerMgr.Models.Customer;
import com.sunbase.CustomerMgr.Repositories.CustomerRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.List;
import java.util.Map;
@Service
public class AuthService {

    @Value("${auth.url}")
    private String authUrl;

    private Logger logger = LoggerFactory.getLogger(AuthService.class);
    public String authenticate() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);

        String authBody = "{\"login_id\" : \"test@sunbasedata.com\", \"password\" :\"Test@123\"}";
        HttpEntity<String> request = new HttpEntity<>(authBody, headers);
        ResponseEntity<Map> response = restTemplate.exchange(authUrl, HttpMethod.POST, request, Map.class);
        logger.info("procedd");
        return response.getBody().get("token").toString();
    }

}
