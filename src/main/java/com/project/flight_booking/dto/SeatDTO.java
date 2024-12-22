package com.project.flight_booking.dto;

import java.math.BigDecimal;

public class SeatDTO {
    private Long id;
    private String seatNumber;
    private BigDecimal price;
    private String status;

    public SeatDTO() {
    }

    public SeatDTO(Long id, String seatNumber, BigDecimal price, String status) {
        this.id = id;
        this.seatNumber = seatNumber;
        this.price = price;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
