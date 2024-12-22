package com.project.flight_booking.service.mapper;

import com.project.flight_booking.dto.FlightDTO;
import com.project.flight_booking.entity.Flight;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class FlightMapper {

    private final SeatMapper seatMapper;

    public FlightMapper(SeatMapper seatMapper) {
        this.seatMapper = seatMapper;
    }

    public FlightDTO toDTO(Flight flight) {
        if (flight == null) {
            return null;
        }

        FlightDTO dto = new FlightDTO();
        dto.setId(flight.getId());
        dto.setFlightNumber(flight.getFlightNumber());
        dto.setDescription(flight.getDescription());
        dto.setDepartureTime(flight.getDepartureTime());
        dto.setArrivalTime(flight.getArrivalTime());
        dto.setBasePrice(flight.getBasePrice());

        // convert seats
        if (flight.getSeats() != null) {
            dto.setSeats(flight.getSeats().stream()
                    .map(seatMapper::toDTO)
                    .collect(Collectors.toSet()));
        }

        return dto;
    }

    public Flight toEntity(FlightDTO dto) {
        if (dto == null) {
            return null;
        }

        Flight flight = new Flight();
        updateEntity(flight, dto);
        return flight;
    }

    public void updateEntity(Flight flight, FlightDTO dto) {
        flight.setFlightNumber(dto.getFlightNumber());
        flight.setDescription(dto.getDescription());
        flight.setDepartureTime(dto.getDepartureTime());
        flight.setArrivalTime(dto.getArrivalTime());
        flight.setBasePrice(dto.getBasePrice());
    }
}
