package com.example.restservice.flights;

import com.example.restservice.bookings.Destination;
import jakarta.persistence.*;

@Entity(name="Flight")
@Table(name = "flights")
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @JoinColumn(name="origin_id", referencedColumnName = "destination_id")
    @OneToOne(targetEntity = Destination.class, fetch = FetchType.EAGER)
    private Destination origin;

    @JoinColumn(name="destination_id", referencedColumnName = "destination_id")
    @OneToOne(targetEntity = Destination.class, fetch = FetchType.EAGER)
    private Destination destination;

    @JoinColumn(name="airport_id")
    @OneToOne(targetEntity = Airport.class, fetch = FetchType.EAGER)
    private Airport airport;

    @Column(name="departure_date")
    private String departureDate;

    @Column(name="departure_time")
    private String departureTime;

    @Column(name="arrival_date")
    private String arrivalDate;

    @Column(name="arrival_time")
    private String arrivalTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Destination getOrigin() {
        return origin;
    }

    public void setOrigin(Destination origin) {
        this.origin = origin;
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    public Airport getAirport() {
        return airport;
    }

    public void setAirport(Airport airport) {
        this.airport = airport;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(String arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
}
