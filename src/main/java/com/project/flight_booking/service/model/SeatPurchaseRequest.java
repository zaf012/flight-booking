package com.project.flight_booking.service.model;

public class SeatPurchaseRequest {
    private Long seatId;
    private Long userId;

    public SeatPurchaseRequest(Long seatId, Long userId) {
        this.seatId = seatId;
        this.userId = userId;
    }

    public Long getSeatId() {
        return seatId;
    }

    public void setSeatId(Long seatId) {
        this.seatId = seatId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}

