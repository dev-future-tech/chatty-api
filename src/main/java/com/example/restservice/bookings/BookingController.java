package com.example.restservice.bookings;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/booking")
public class BookingController {

    private final Logger log = LoggerFactory.getLogger(BookingController.class);

    BookingRepository bookingRepository;

    public BookingController(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<List<CustomerBooking>> getCustomerBookingsForCustomer(
            @PathVariable("customerId") Integer customerId) {
        log.debug("Loading bookings for customer {}", customerId);
        List<CustomerBooking> bookings = this.bookingRepository.findCustomerBookingByCustomerId(customerId);
        return ResponseEntity.ok(bookings);
    }

    record BookingRequest(String email, @JsonProperty("start_date") String startDate,
                          @JsonProperty("end_date") String endDate,
                          @JsonProperty("destination_id") Integer destinatonId) {};

    @PostMapping()
    public ResponseEntity<Void> createBooking(@RequestBody BookingRequest request) {
        log.debug("Creating booking for {}", request.email());
        return ResponseEntity.ok().build();
    }
}
