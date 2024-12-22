package com.project.flight_booking.service;

import com.project.flight_booking.service.model.PurchaseResult;
import com.project.flight_booking.service.model.SeatPurchaseRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Service
public class SimulationPurchaseSeatService {

    private final SeatService seatService;
    private final Logger log = LoggerFactory.getLogger(SimulationPurchaseSeatService.class);

    public SimulationPurchaseSeatService(SeatService seatService) {
        this.seatService = seatService;
    }

    public List<PurchaseResult> simulateConcurrentPurchases(List<SeatPurchaseRequest> requests) {
        List<PurchaseResult> results = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(requests.size());
        CountDownLatch latch = new CountDownLatch(requests.size());

        // Her bir istek için Future oluştur
        List<Future<PurchaseResult>> futures = new ArrayList<>();

        // Her bir isteği ayrı thread'de çalıştır
        for (SeatPurchaseRequest request : requests) {
            Future<PurchaseResult> future = executorService.submit(() -> {
                try {
                    log.info("Thread {} starting purchase attempt for user {} and seat {}",
                            Thread.currentThread().getName(), request.getUserId(), request.getSeatId());

                    // Rastgele küçük bir gecikme ekle (0-100ms)
                    Thread.sleep(ThreadLocalRandom.current().nextInt(100));

                    // Koltuk satın alma işlemini dene
                    seatService.purchaseSeat(request.getSeatId());

                    log.info("Thread {} successfully purchased seat {} for user {}",
                            Thread.currentThread().getName(), request.getSeatId(), request.getUserId());

                    return new PurchaseResult(
                            request.getUserId(),
                            request.getSeatId(),
                            true,
                            "Seat is successfully purchased : " + request.getSeatId()
                    );

                } catch (Exception e) {
                    log.error("Thread {} failed to purchase seat {} for user {}: {}",
                            Thread.currentThread().getName(), request.getSeatId(),
                            request.getUserId(), e.getMessage());

                    return new PurchaseResult(
                            request.getUserId(),
                            request.getSeatId(),
                            false,
                            "Satın alma başarısız: " + e.getMessage()
                    );
                } finally {
                    latch.countDown();
                }
            });

            futures.add(future);
        }

        // Tüm thread'lerin tamamlanmasını bekle
        try {
            latch.await(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.error("Simulation interrupted", e);
        }

        // Sonuçları topla
        for (Future<PurchaseResult> future : futures) {
            try {
                results.add(future.get());
            } catch (Exception e) {
                log.error("Error getting future result", e);
            }
        }

        executorService.shutdown();
        return results;
    }

}
