package com.example.restservice.bookings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BookingController {

    private final Logger log = LoggerFactory.getLogger(BookingController.class);

    BookingRepository bookingRepository;

    public BookingController(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @GetMapping("/booking/{customerId}")
    public ResponseEntity<List<CustomerBooking>> getCustomerBookingsForCustomer(
            @PathVariable("customerId") Integer customerId) {
        log.debug("Loading bookings for customer {}", customerId);
        List<CustomerBooking> bookings = this.bookingRepository.findCustomerBookingByCustomerId(customerId);
        return ResponseEntity.ok(bookings);
    }
}
