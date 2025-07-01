package com.example.restservice.flights;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/flight")
public class FlightController {

    final FlightRepository flightRepository;

    public FlightController(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @GetMapping
    public ResponseEntity<List<Flight>> getFlights(@RequestParam(value = "airport_id", required = false) Long airportId) {
        List<Flight> allFlights;
        if( airportId == null ) {
            allFlights = this.flightRepository.findAll();
        } else {
            allFlights = this.flightRepository.getFlightsByAirport(airportId);
        }

        return ResponseEntity.ok(allFlights);
    }

}
