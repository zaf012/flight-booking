package com.project.flight_booking.controller;

import com.project.flight_booking.dto.SeatDTO;
import com.project.flight_booking.entity.Seat;
import com.project.flight_booking.service.SeatService;
import com.project.flight_booking.service.mapper.SeatMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/seats")

public class SeatController {

    private final SeatService seatService;
    private final SeatMapper seatMapper;

    public SeatController(SeatService seatService, SeatMapper seatMapper) {
        this.seatService = seatService;
        this.seatMapper = seatMapper;
    }

    @PostMapping("/{flightId}/create-seat")
    public ResponseEntity<SeatDTO> createSeat(@PathVariable Long flightId,
                                              @RequestBody SeatDTO seatDTO) {
        Seat seat = seatMapper.toEntity(seatDTO);
        Seat savedSeat = seatService.createSeat(seat, flightId);
        return ResponseEntity.ok(seatMapper.toDTO(savedSeat));
    }

    @GetMapping("/get-seat/{id}")
    public ResponseEntity<SeatDTO> getSeat(@PathVariable Long id) {
        Seat seat = seatService.getSeat(id);
        return ResponseEntity.ok(seatMapper.toDTO(seat));
    }

    @PostMapping("/purchase-seat/{id}")
    public ResponseEntity<SeatDTO> purchaseSeat(@PathVariable Long id) {
        try {
            Seat purchasedSeat = seatService.purchaseSeat(id);
            return ResponseEntity.ok(seatMapper.toDTO(purchasedSeat));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }


    @DeleteMapping("/delete-seat/{id}")
    public ResponseEntity<Void> deleteSeat(@PathVariable Long id) {
        seatService.deleteSeat(id);
        return ResponseEntity.ok().build();
    }
}
