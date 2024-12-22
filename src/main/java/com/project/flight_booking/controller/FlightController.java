package com.project.flight_booking.controller;

import com.project.flight_booking.dto.FlightDTO;
import com.project.flight_booking.entity.Flight;
import com.project.flight_booking.service.FlightService;
import com.project.flight_booking.service.mapper.FlightMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/flights")
public class FlightController {
    private final FlightService flightService;
    private final FlightMapper flightMapper;

    public FlightController(FlightService flightService, FlightMapper flightMapper) {
        this.flightService = flightService;
        this.flightMapper = flightMapper;
    }

    @GetMapping("/get-flight/{id}")
    public ResponseEntity<FlightDTO> getFlight(@PathVariable Long id) {
        Flight flight = flightService.getFlight(id);
        return ResponseEntity.ok(flightMapper.toDTO(flight));
    }

    @GetMapping("/get-all-flights")
    public ResponseEntity<List<FlightDTO>> getAllFlights() {
        List<Flight> flights = flightService.getAllFlights();
        List<FlightDTO> flightDTOs = flights.stream()
                .map(flightMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(flightDTOs);
    }

    @PostMapping("/create-flight")
    public ResponseEntity<FlightDTO> createFlight(@RequestBody FlightDTO flightDTO) {
        Flight flight = flightMapper.toEntity(flightDTO);
        Flight savedFlight = flightService.createFlight(flight);
        return ResponseEntity.ok(flightMapper.toDTO(savedFlight));
    }

    @PutMapping("/update-flight/{id}")
    public ResponseEntity<FlightDTO> updateFlight(@PathVariable Long id, @RequestBody FlightDTO flightDTO) {
        Flight updatedFlight = flightService.updateFlight(id, flightMapper.toEntity(flightDTO));
        return ResponseEntity.ok(flightMapper.toDTO(updatedFlight));
    }

    @DeleteMapping("/delete-flight/{id}")
    public ResponseEntity<Void> deleteFlight(@PathVariable Long id) {
        flightService.deleteFlight(id);
        return ResponseEntity.ok().build();
    }
}

