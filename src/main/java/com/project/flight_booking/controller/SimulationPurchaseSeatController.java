package com.project.flight_booking.controller;

import com.project.flight_booking.service.SimulationPurchaseSeatService;
import com.project.flight_booking.service.model.PurchaseResult;
import com.project.flight_booking.service.model.SeatPurchaseRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/simulation")
public class SimulationPurchaseSeatController {
    private final SimulationPurchaseSeatService simulationPurchaseSeatService;

    public SimulationPurchaseSeatController(SimulationPurchaseSeatService simulationPurchaseSeatService) {
        this.simulationPurchaseSeatService = simulationPurchaseSeatService;
    }


    @PostMapping("/concurrent-purchase")
    public ResponseEntity<List<PurchaseResult>> simulateConcurrentPurchase() {
        // İki farklı kullanıcı için istek oluştur
        List<SeatPurchaseRequest> requests = Arrays.asList(
                new SeatPurchaseRequest(98L, 5L),    // Thread-1: SeatId: 10, userId: 5
                new SeatPurchaseRequest(98L, 218L)   // Thread-2: SeatId: 10, userId: 218
        );

        List<PurchaseResult> results = simulationPurchaseSeatService.simulateConcurrentPurchases(requests);
        return ResponseEntity.ok(results);
    }
}

