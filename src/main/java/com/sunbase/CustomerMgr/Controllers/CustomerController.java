package com.sunbase.CustomerMgr.Controllers;

import com.sunbase.CustomerMgr.DTOs.CustomerDto;
import com.sunbase.CustomerMgr.Models.Customer;
import com.sunbase.CustomerMgr.Services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {


    @Autowired
    CustomerService customerService;

    @PostMapping("/create")
    public ResponseEntity<String> create_user(@RequestBody Customer customer){

        try{

            String response = customerService.create_user(customer);
            return new ResponseEntity<>(response , HttpStatus.CREATED);

        }catch (Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e.getMessage() , HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateCustomer(@PathVariable int id, @RequestBody Customer customer) {
        try{

            String response = customerService.updateCustomer(id, customer);
            return new ResponseEntity<>(response , HttpStatus.CREATED);

        }catch (Exception e){
            if(e.getMessage().equals("Not Found")){
                return new ResponseEntity<>("customer not found...! Please provide correct customer id" , HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(e.getMessage() , HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/get/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable int id) {

        try{
            Customer customer = customerService.getCustomerById(id);
            return new ResponseEntity<>(customer , HttpStatus.FOUND);
        }catch (Exception e){
            return new ResponseEntity<>(null , HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/find")
    public ResponseEntity<List<Customer>> getCustomers(@RequestParam(defaultValue = "1") int limit , @RequestParam(defaultValue = "10") int page){

        try{
            List<Customer> customers = customerService.getCustomers(limit , page);

            return new ResponseEntity<>(customers , HttpStatus.FOUND);
        }catch (Exception e){
            return new ResponseEntity<>(null , HttpStatus.NOT_FOUND);
        }
    }
//
    @DeleteMapping("/remove")
    public ResponseEntity<String> deleteCustomer(@RequestParam int id) {

        try{
            customerService.deleteCustomer(id);
            return new ResponseEntity<>("Customer is successfully removed" , HttpStatus.OK);

        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage() , HttpStatus.FORBIDDEN);
        }

    }

    @PostMapping("/sync")
    public ResponseEntity<List<Customer>> syncCustomers() {

        try{
            List<Customer> customers = customerService.syncCustomers();
            return new ResponseEntity<>(customers, HttpStatus.OK);

        }catch (Exception e){
            return new ResponseEntity<>(null , HttpStatus.BAD_REQUEST);
        }

    }

}
