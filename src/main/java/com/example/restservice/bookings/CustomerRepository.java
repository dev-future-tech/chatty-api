package com.example.restservice.bookings;

import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    Customer findCustomerNameByCustomerId(Integer customerId);
    Customer findCustomerByEmail(String email);
}
