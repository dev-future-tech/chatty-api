package com.example.restservice.bookings;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/booking")
public class BookingController {

    private final Logger log = LoggerFactory.getLogger(BookingController.class);

    final BookingRepository bookingRepository;
    final CustomerRepository customerRepository;
    final DestinationRepository destinationRepository;

    public BookingController(BookingRepository bookingRepository, CustomerRepository customerRepository,
                             DestinationRepository destinationRepository) {
        this.bookingRepository = bookingRepository;
        this.customerRepository = customerRepository;
        this.destinationRepository = destinationRepository;
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
                          @JsonProperty("destination_id") Integer destinationId) {};


    @PostMapping(consumes = {"application/json"})
    public ResponseEntity<String> createBooking(@RequestBody BookingRequest request) {
        log.info("Creating booking for {}", request.email());
        var customer = customerRepository.findCustomerByEmail(request.email());
        var destination = destinationRepository.findByDestinationId(request.destinationId().longValue());
        if (customer == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Customer not found");
        }

        if(destination == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Destination not found");
        }

        var booking = new CustomerBooking();
        booking.setCustomer(customer);
        booking.setDestination(destination.getCity());
        booking.setStartDate(LocalDate.parse(request.startDate()));
        booking.setEndDate(LocalDate.parse(request.endDate()));
        booking.setTotalCost(0.0);
        var saved = bookingRepository.save(booking);

        return ResponseEntity.created(URI.create(String.format("/booking/%d", saved.getBookingId()))).build();
    }
}
