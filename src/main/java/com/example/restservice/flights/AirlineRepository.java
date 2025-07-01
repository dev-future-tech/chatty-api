package com.example.restservice.flights;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AirlineRepository extends JpaRepository<Airport,Long> {

}
