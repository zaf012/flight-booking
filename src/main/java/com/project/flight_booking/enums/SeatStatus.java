package com.project.flight_booking.enums;

import java.util.HashMap;
import java.util.Map;

//    AVAILABLE,
//    RESERVED,
//    SOLD

public enum SeatStatus {
    AVAILABLE("PENDING_APPROVAL"),
    RESERVED("MOVED_TO_LOADED_FOLDER"),
    SOLD("APPROVED");

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
