package com.sunbase.CustomerMgr.Repositories;

import com.sunbase.CustomerMgr.Models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepo extends JpaRepository<Customer , Integer> {

    @Query(value = "SELECT * FROM customer LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<Customer> findCustomersWithPagination(@Param("limit") int limit, @Param("offset") int offset);

    @Query(value = "SELECT * FROM customer WHERE email : email", nativeQuery = true)
    List<Customer> findByEmail(@Param("email") String email);
}
