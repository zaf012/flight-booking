package com.project.flight_booking.service.model;

public class PurchaseResult {

    private Long userId;
    private Long seatId;
    private boolean success;
    private String message;

    public PurchaseResult(Long userId, Long seatId, boolean success, String message) {
        this.userId = userId;
        this.seatId = seatId;
        this.success = success;
        this.message = message;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getSeatId() {
        return seatId;
    }

    public void setSeatId(Long seatId) {
        this.seatId = seatId;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
