package com.example.restservice.bookings;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/destinations")
public class DestinationsController {

    DestinationRepository destinationRepository;

    public DestinationsController(DestinationRepository repository) {
        this.destinationRepository = repository;
    }

    @GetMapping
    public List<Destination> findAll() {
        return this.destinationRepository.findAll();
    }

    @GetMapping("/{destinationId}")
    public Destination findOne(@PathVariable("destinationId") Long destinationId) {
        return destinationRepository.findByDestinationId(destinationId);
    }
}
