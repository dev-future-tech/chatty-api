package com.example.restservice.tools;

import com.example.restservice.bookings.Destination;
import com.example.restservice.bookings.DestinationRepository;
import com.example.restservice.flights.Airport;
import com.example.restservice.flights.AirportRepository;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DestinationTool {

    DestinationRepository destinationRepository;
    AirportRepository airportRepository;

    @Autowired
    public DestinationTool(DestinationRepository destinationRepository,  AirportRepository airportRepository) {
        this.destinationRepository = destinationRepository;
        this.airportRepository = airportRepository;
    }

    @Tool
    public List<Destination> getDestinations() {
        return destinationRepository.findAll();
    }

    @Tool
    public List<Airport> getAirports() {
        return airportRepository.findAll();
    }
}
