package com.project.flight_booking.service.mapper;

import com.project.flight_booking.dto.SeatDTO;
import com.project.flight_booking.enums.SeatStatus;
import com.project.flight_booking.entity.Seat;
import org.springframework.stereotype.Component;

@Component
public class SeatMapper {

    public SeatDTO toDTO(Seat seat) {
        if (seat == null) {
            return null;
        }

        SeatDTO dto = new SeatDTO();
        dto.setId(seat.getId());
        dto.setSeatNumber(seat.getSeatNumber());
        dto.setPrice(seat.getPrice());
        dto.setStatus(seat.getStatus().name());

        return dto;
    }

    public Seat toEntity(SeatDTO dto) {
        if (dto == null) {
            return null;
        }

        Seat seat = new Seat();
        updateEntity(seat, dto);
        return seat;
    }

    public void updateEntity(Seat seat, SeatDTO dto) {
        seat.setSeatNumber(dto.getSeatNumber());
        seat.setPrice(dto.getPrice());
        if (dto.getStatus() != null) {
            seat.setStatus(SeatStatus.valueOf(dto.getStatus()));
        }
    }
}
