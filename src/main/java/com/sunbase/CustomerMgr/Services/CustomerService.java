package com.sunbase.CustomerMgr.Services;

import com.sunbase.CustomerMgr.DTOs.CustomerDto;
import com.sunbase.CustomerMgr.Models.Customer;
import com.sunbase.CustomerMgr.Repositories.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CustomerService {

    @Autowired
    CustomerRepo customerRepo;

    @Autowired
    AuthService authService;

    @Value("${customer.url}")
    private String customerUrl;
    public String create_user(Customer customer) throws Exception{

//        Customer customer = Customer.builder()
//                .first_name(customerDto.getFirst_name())
//                .last_name(customerDto.getLast_name())
//                .address(customerDto.getAddress())
//                .state(customerDto.getState())
//                .email(customerDto.getEmail())
//                .phone(customerDto.getPhone())
//                .street(customerDto.getStreet())
//                .city(customerDto.getCity())
//                .build();

        customerRepo.save(customer);
        return "User is added";
    }
    public String updateCustomer(int id, Customer customer) throws Exception {

        Customer DbCustomer;

        try{
            DbCustomer = customerRepo.findById(id).get();
        }catch (Exception e){
            throw new Exception("Not Found");
        }
        customer.setId(DbCustomer.getId());
        customerRepo.save(customer);
        return "customer is updated";
    }

    public void deleteCustomer(int cust_id){
        customerRepo.deleteById(cust_id);
    }

    public Customer getCustomerById(int id){
        return customerRepo.findById(id).get();
    }
    public List<Customer> getCustomers(int limit , int page){

        int offset = (page - 1) * limit;
        return customerRepo.findCustomersWithPagination(limit , offset);
    }

    public void syncCustomers() {
        String token = authService.authenticate();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        System.out.println("token");
        System.out.println(token);
        headers.set("Authorization", "Bearer " + token);
//        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<List> response = restTemplate.exchange(customerUrl + "?cmd=get_customer_list", HttpMethod.GET, entity, List.class);
        List<Map<String, Object>> customers = response.getBody();

        for (Map<String, Object> customerData : customers) {
            Customer customer = mapToCustomer(customerData);
            customerRepo.save(customer); // Assuming save() method handles both insert and update
        }
    }

    private Customer mapToCustomer(Map<String, Object> customerData) {
        Customer customer = new Customer();
//        customer.setUuid((String) customerData.get("uuid"));
        customer.setFirst_name((String) customerData.get("first_name"));
        customer.setLast_name((String) customerData.get("last_name"));
        customer.setStreet((String) customerData.get("street"));
        customer.setAddress((String) customerData.get("address"));
        customer.setCity((String) customerData.get("city"));
        customer.setState((String) customerData.get("state"));
        customer.setEmail((String) customerData.get("email"));
        customer.setPhone((String) customerData.get("phone"));
        return customer;
    }
}
