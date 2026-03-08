package pe.com.hotel.reservation.reservation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.com.hotel.reservation.reservation.dto.CreateReservationRequest;
import pe.com.hotel.reservation.reservation.dto.ReservationResponse;
import pe.com.hotel.reservation.reservation.dto.UpdateReservationRequest;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    // GET /api/reservations
    @GetMapping
    public ResponseEntity<Page<ReservationResponse>> getAllReservations(Pageable pageable) {
        return ResponseEntity.ok(reservationService.getAllReservations(pageable));
    }

    // GET /api/reservations/5
    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponse> getReservationById(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.getReservationById(id));
    }

    // GET /api/reservations/guest/3
    @GetMapping("/guest/{guestId}")
    public ResponseEntity<Page<ReservationResponse>> getReservationsByGuest(
            @PathVariable Long guestId, Pageable pageable) {
        return ResponseEntity.ok(reservationService.getReservationsByGuest(guestId, pageable));
    }

    // GET /api/reservations/room/7
    @GetMapping("/room/{roomId}")
    public ResponseEntity<Page<ReservationResponse>> getReservationsByRoom(
            @PathVariable Long roomId, Pageable pageable) {
        return ResponseEntity.ok(reservationService.getReservationsByRoom(roomId, pageable));
    }

    // POST /api/reservations
    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(
            @Valid @RequestBody CreateReservationRequest request) {
        ReservationResponse created = reservationService.createReservation(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // PUT /api/reservations/5
    @PutMapping("/{id}")
    public ResponseEntity<ReservationResponse> updateReservation(
            @PathVariable Long id,
            @Valid @RequestBody UpdateReservationRequest request) {
        return ResponseEntity.ok(reservationService.updateReservation(id, request));
    }

    // PATCH /api/reservations/5/confirm
    @PatchMapping("/{id}/confirm")
    public ResponseEntity<ReservationResponse> confirmReservation(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.confirmReservation(id));
    }

    // PATCH /api/reservations/5/cancel
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ReservationResponse> cancelReservation(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.cancelReservation(id));
    }

    // PATCH /api/reservations/5/complete
    @PatchMapping("/{id}/complete")
    public ResponseEntity<ReservationResponse> completeReservation(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.completeReservation(id));
    }

    // DELETE /api/reservations/5
    @DeleteMapping("/{id}")
    public ResponseEntity<ReservationResponse> deleteReservation(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.deleteReservation(id));
    }

    // PATCH /api/reservations/5/enable
    @PatchMapping("/{id}/enable")
    public ResponseEntity<ReservationResponse> enableReservation(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.enableReservation(id));
    }
}