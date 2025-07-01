package com.example.restservice.flights;

import jakarta.persistence.NamedQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FlightRepository extends JpaRepository<Flight,Long> {

    @Query("Select f from  Flight f where f.airport.airportId = ?1")
    List<Flight> getFlightsByAirport(Long airportId);
}
