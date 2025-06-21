package com.example.restservice.tools;

import com.example.restservice.bookings.BookingRepository;
import com.example.restservice.bookings.CustomerBooking;
import com.example.restservice.bookings.Destination;
import com.example.restservice.bookings.DestinationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomerBookingTools {

    private final Logger log = LoggerFactory.getLogger(CustomerBookingTools.class);

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    DestinationRepository destinationRepository;

    @Tool
    public List<CustomerBooking> findCustomerBookingByCustomerId(@ToolParam int customerId) {
        log.info("Looking for customer {} bookings...", customerId);
        return  bookingRepository.findCustomerBookingByCustomerId(customerId);
    }

    @Tool
    public List<Destination> getAllDestinations() {
        return destinationRepository.findAll();
    }

    @Tool
    public String getSeason(@ToolParam Integer month) {
        if (
                month ==1 | month == 11 | month ==12
        ) return "winter";

        if (
                month == 2 | month == 3 | month == 4
        ) return "spring";

        if (
                month == 5 || month == 6 || month == 7
        ) return "summer";

        return "fall";
    }
}
