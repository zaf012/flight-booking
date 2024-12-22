package com.project.flight_booking;

import com.project.flight_booking.entity.Seat;
import com.project.flight_booking.enums.SeatStatus;
import com.project.flight_booking.service.SeatService;
import com.project.flight_booking.service.SimulationPurchaseSeatService;
import com.project.flight_booking.service.model.PurchaseResult;
import com.project.flight_booking.service.model.SeatPurchaseRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
class FlightBookingApplicationTests {

	@Autowired
	private SimulationPurchaseSeatService simulationService;

	@MockBean
	private SeatService seatService;

	private Seat testSeat;

	@BeforeEach
	void setUp() {
		// Test verileri
		testSeat = new Seat();
		testSeat.setId(10L);
		testSeat.setSeatNumber("A1");
		testSeat.setPrice(new BigDecimal("100.00"));
		testSeat.setStatus(SeatStatus.AVAILABLE);
	}

	@Test
	void testConcurrentPurchaseSimulation() {
		// İlk çağrı için başarılı senaryo
		when(seatService.purchaseSeat(10L))
				.thenReturn(testSeat)  // İlk çağrı için başarılı
				.thenThrow(new RuntimeException("Seat is already purchased")); // İkinci çağrı için hata

		// Test verileri
		List<SeatPurchaseRequest> requests = Arrays.asList(
				new SeatPurchaseRequest(10L, 5L),    // Thread-1: SeatId: 10, userId: 5
				new SeatPurchaseRequest(10L, 218L)   // Thread-2: SeatId: 10, userId: 218
		);

		// Çalıştır
		List<PurchaseResult> results = simulationService.simulateConcurrentPurchases(requests);

		// Sonuçları doğrula
		assertEquals(2, results.size(), "İki sonuç olmalı");

		// Başarılı ve başarısız işlem sayılarını kontrol et
		long successCount = results.stream().filter(PurchaseResult::isSuccess).count();
		long failureCount = results.stream().filter(result -> !result.isSuccess()).count();

		assertEquals(1, successCount, "Sadece bir başarılı işlem olmalı");
		assertEquals(1, failureCount, "Sadece bir başarısız işlem olmalı");

		// Başarılı ve başarısız işlemlerin mesajlarını kontrol et
		results.forEach(result -> {
			if (result.isSuccess()) {
				assertTrue(result.getMessage().contains("successfully purchased"),
						"Başarılı işlem mesajı doğru olmalı");
			} else {
				assertTrue(result.getMessage().contains("Satın alma başarısız"),
						"Başarısız işlem mesajı doğru olmalı");
			}
		});
	}

	@Test
	void testAllPurchasesFail() {
		// Tüm çağrılar için hata senaryosu
		when(seatService.purchaseSeat(anyLong()))
				.thenThrow(new RuntimeException("Seat not available"));

		List<SeatPurchaseRequest> requests = Arrays.asList(
				new SeatPurchaseRequest(10L, 5L),
				new SeatPurchaseRequest(10L, 218L)
		);

		List<PurchaseResult> results = simulationService.simulateConcurrentPurchases(requests);

		assertEquals(2, results.size());
		assertTrue(results.stream().noneMatch(PurchaseResult::isSuccess),
				"Hiçbir işlem başarılı olmamalı");
	}
}
