package com.sunbase.CustomerMgr.Services;

import com.sunbase.CustomerMgr.DTOs.CustomerDto;
import com.sunbase.CustomerMgr.Models.Customer;
import com.sunbase.CustomerMgr.Repositories.CustomerRepo;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class CustomerService {

    private Logger logger = LoggerFactory.getLogger(CustomerService.class);
    @Autowired
    CustomerRepo customerRepo;

    @Autowired
    AuthService authService;

    @Autowired
    RestTemplate restTemplate;

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
    public void saveOrUpdateCustomers(List<Customer> customers) {
        for (Customer customer : customers) {
            customerRepo.findByPhone(customer.getPhone())
                    .ifPresentOrElse(
                            existingCustomer -> {
                                existingCustomer.setFirst_name(customer.getFirst_name());
                                existingCustomer.setLast_name(customer.getLast_name());
                                existingCustomer.setStreet(customer.getStreet());
                                existingCustomer.setAddress(customer.getAddress());
                                existingCustomer.setCity(customer.getCity());
                                existingCustomer.setState(customer.getState());
                                existingCustomer.setEmail(customer.getEmail());
                                customerRepo.save(existingCustomer);
                            },
                            () -> customerRepo.save(customer)
                    );
        }
    }

    public List<Customer> syncCustomers() {
        String token = authService.authenticate();
        logger.info(token);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        Customer[] customers = restTemplate.exchange(
                "https://qa.sunbasedata.com/sunbase/portal/api/assignment.jsp?cmd=get_customer_list",
                HttpMethod.GET,
                entity,
                Customer[].class
        ).getBody();
        logger.info("frd:{}" , customers);
        this.saveOrUpdateCustomers(Arrays.asList(customers));
        return Arrays.asList(customers);
    }
}
