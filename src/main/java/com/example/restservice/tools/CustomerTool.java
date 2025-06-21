package com.example.restservice.tools;

import com.example.restservice.bookings.CustomerRepository;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomerTool {

    @Autowired
    CustomerRepository customerRepository;

    @Tool
    public String getCustomerName(@ToolParam Integer customerId) {
        return customerRepository.findCustomerNameByCustomerId(customerId).getCustomerName();
    }
}
