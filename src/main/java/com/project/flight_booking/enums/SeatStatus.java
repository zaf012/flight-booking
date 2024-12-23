package com.project.flight_booking.enums;

import java.util.HashMap;
import java.util.Map;

//    AVAILABLE,
//    SOLD

public enum SeatStatus {
    AVAILABLE("AVAILABLE"),
    SOLD("SOLD");

    private static final Map<String, SeatStatus> lookup = new HashMap<>();

    static {
        for (SeatStatus source : SeatStatus.values()) {
            lookup.put(source.getSeatStatus(), source);
        }
    }

    private final String seatStatus;

    SeatStatus(String seatStatus) {
        this.seatStatus = seatStatus;
    }

    public String getSeatStatus() {
        return seatStatus;
    }
}
