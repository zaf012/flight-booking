package com.project.flight_booking.service;


import com.project.flight_booking.entity.Flight;
import com.project.flight_booking.exception.ValidationException;
import com.project.flight_booking.repository.FlightRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;

@Service
public class FlightService {

    private final FlightRepository flightRepository;

    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    private void validateFlight(Flight flight) {
        if (flight == null) {
            throw new ValidationException("Flight cannot be null");
        }
        if (!StringUtils.hasText(flight.getFlightNumber())) {
            throw new ValidationException("Flight number cannot be empty");
        }
        if (!StringUtils.hasText(flight.getDescription())) {
            throw new ValidationException("Flight description cannot be empty");
        }
        if (flight.getDepartureTime() == null) {
            throw new ValidationException("Departure time cannot be null");
        }
        if (flight.getArrivalTime() == null) {
            throw new ValidationException("Arrival time cannot be null");
        }
        if (flight.getBasePrice() == null || flight.getBasePrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Base price must be greater than zero");
        }
        if (flight.getDepartureTime().isAfter(flight.getArrivalTime())) {
            throw new ValidationException("Departure time cannot be after arrival time");
        }
    }

    @Transactional(readOnly = true)
    public Flight getFlight(Long id) {
        if (id == null) {
            throw new ValidationException("Flight ID cannot be null");
        }
        return flightRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Flight is not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<Flight> getAllFlights() {
        return flightRepository.findAll();
    }

    @Transactional
    public Flight createFlight(Flight flight) {
        validateFlight(flight);
        return flightRepository.save(flight);
    }

    @Transactional
    public Flight updateFlight(Long id, Flight flightDetails) {
        if (id == null) {
            throw new ValidationException("Flight ID cannot be null");
        }
        validateFlight(flightDetails);

        Flight existingFlight = flightRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Flight is not found"));

        existingFlight.setFlightNumber(flightDetails.getFlightNumber());
        existingFlight.setDescription(flightDetails.getDescription());
        existingFlight.setDepartureTime(flightDetails.getDepartureTime());
        existingFlight.setArrivalTime(flightDetails.getArrivalTime());
        existingFlight.setBasePrice(flightDetails.getBasePrice());

        return flightRepository.save(existingFlight);
    }

    @Transactional
    public void deleteFlight(Long id) {
        if (id == null) {
            throw new ValidationException("Flight ID cannot be null");
        }
        if (!flightRepository.existsById(id)) {
            throw new ValidationException("Flight not found with id: " + id);
        }
        flightRepository.deleteById(id);
    }
}
