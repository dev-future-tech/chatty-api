package com.example.restservice.bookings;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import java.time.LocalDate;


@Entity
@Table(name="customer_booking")
public class CustomerBooking {

    @Id
    @Column(name="booking_id")
    private Integer bookingId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="customer_id", referencedColumnName = "customer_id")
    @JsonIgnore
    private Customer customer;

    private String destination;

    @Convert(converter = Jsr310JpaConverters.LocalDateConverter.class)
    @Column(name="start_date")
    @JsonProperty("start_date")
    private java.time.LocalDate startDate;

    @Convert(converter = Jsr310JpaConverters.LocalDateConverter.class)
    @Column(name="end_date")
    @JsonProperty("end_date")
    private java.time.LocalDate endDate;

    @Column(name="total_cost")
    @JsonProperty("total_cost")
    private Double totalCost;

    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }
}
