package com.project.flight_booking.service;

import com.project.flight_booking.entity.Flight;
import com.project.flight_booking.entity.Seat;
import com.project.flight_booking.enums.SeatStatus;
import com.project.flight_booking.exception.ValidationException;
import com.project.flight_booking.repository.FlightRepository;
import com.project.flight_booking.repository.SeatRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

@Service
public class SeatService {

    private static final Logger log = LoggerFactory.getLogger(SeatService.class);
    private final SeatRepository seatRepository;
    private final FlightRepository flightRepository;


    public SeatService(SeatRepository seatRepository, FlightRepository flightRepository) {
        this.seatRepository = seatRepository;
        this.flightRepository = flightRepository;
    }

    private void validateSeat(Seat seat, Long flightId) {
        if (seat == null) {
            throw new ValidationException("Seat cannot be null");
        }
        if (!StringUtils.hasText(seat.getSeatNumber())) {
            throw new ValidationException("Seat number cannot be empty");
        }
        if (seat.getPrice() == null || seat.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Seat price must be greater than zero");
        }
        if (flightId == null) {
            throw new ValidationException("Flight ID cannot be null");
        }

        // Aynı uçuşta aynı koltuk numarası kontrolü
        if (seatRepository.existsByFlightIdAndSeatNumber(flightId, seat.getSeatNumber())) {
            throw new ValidationException(
                    String.format("Seat number %s already exists for flight %d", seat.getSeatNumber(), flightId)
            );
        }
    }

    @Transactional
    public Seat createSeat(Seat seat, Long flightId) {
        validateSeat(seat, flightId);

        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new ValidationException("Flight not found with id: " + flightId));

        seat.setFlight(flight);
        seat.setStatus(SeatStatus.AVAILABLE);
        return seatRepository.save(seat);
    }

    @Transactional(readOnly = true)
    public Seat getSeat(Long id) {
        if (id == null) {
            throw new ValidationException("Seat ID cannot be null");
        }
        return seatRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Seat is not found with id: " + id));
    }

    @Transactional
    public void deleteSeat(Long id) {
        if (id == null) {
            throw new ValidationException("Seat ID cannot be null");
        }
        Seat seat = seatRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Seat not found with id: " + id));

        if (seat.getStatus() == SeatStatus.SOLD) {
            throw new ValidationException("Cannot delete a sold seat");
        }

        seatRepository.deleteById(id);
    }

    @Transactional
    public Seat purchaseSeat(Long seatId) {
        if (seatId == null) {
            throw new ValidationException("Seat ID cannot be null");
        }

        try {
            Seat seat = seatRepository.findByIdAndStatus(seatId, SeatStatus.AVAILABLE)
                    .orElseThrow(() -> new ValidationException("Seat is not available for purchase"));

            seat.setStatus(SeatStatus.SOLD);
            return seatRepository.save(seat);
        } catch (Exception e) {
            log.error("Error purchasing seat with id {} : {}", seatId, e.getMessage());
            throw new ValidationException("Seat has already been purchased by another customer");
        }
    }
}
