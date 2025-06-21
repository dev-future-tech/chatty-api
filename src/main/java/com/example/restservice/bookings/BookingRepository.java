package com.example.restservice.bookings;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<CustomerBooking, Integer> {

    @Query(value = "Select b from CustomerBooking b where b.customer.customerId = ?1")
    List<CustomerBooking> findCustomerBookingByCustomerId(int customerId);
}
