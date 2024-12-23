package com.project.flight_booking.repository;

import com.project.flight_booking.entity.Seat;
import com.project.flight_booking.enums.SeatStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat, Long> {


    @Lock(LockModeType.OPTIMISTIC)
    Optional<Seat> findByIdAndStatus(Long id, SeatStatus status);

    @Query("SELECT COUNT(s) > 0 FROM Seat s WHERE s.flight.id = :flightId AND s.seatNumber = :seatNumber")
    boolean existsByFlightIdAndSeatNumber(@Param("flightId") Long flightId, @Param("seatNumber") String seatNumber);
}