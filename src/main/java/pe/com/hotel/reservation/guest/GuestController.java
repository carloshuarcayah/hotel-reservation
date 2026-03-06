package pe.com.hotel.reservation.guest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.com.hotel.reservation.guest.dto.CreateGuestRequest;
import pe.com.hotel.reservation.guest.dto.GuestResponse;
import pe.com.hotel.reservation.guest.dto.UpdateGuestRequest;

@RestController
@RequestMapping("/api/guests")
@RequiredArgsConstructor
public class GuestController {

    private final GuestService guestService;

    // GET /api/guests
    @GetMapping
    public ResponseEntity<Page<GuestResponse>> getAllGuests(Pageable pageable) {
        return ResponseEntity.ok(guestService.getAllGuests(pageable));
    }

    // GET /api/guests/5
    @GetMapping("/{id}")
    public ResponseEntity<GuestResponse> getGuestById(@PathVariable Long id) {
        return ResponseEntity.ok(guestService.getGuestById(id));
    }

    // GET /api/guests/document/12345678
    @GetMapping("/document/{documentNumber}")
    public ResponseEntity<GuestResponse> getGuestByDocumentNumber(
            @PathVariable String documentNumber) {
        return ResponseEntity.ok(guestService.getGuestByDocumentNumber(documentNumber));
    }

    // POST /api/guests
    @PostMapping
    public ResponseEntity<GuestResponse> createGuest(
            @Valid @RequestBody CreateGuestRequest request) {
        GuestResponse created = guestService.createGuest(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // PUT /api/guests/5
    @PutMapping("/{id}")
    public ResponseEntity<GuestResponse> updateGuest(
            @PathVariable Long id,
            @Valid @RequestBody UpdateGuestRequest request) {
        return ResponseEntity.ok(guestService.updateGuest(id, request));
    }

    // DELETE /api/guests/5
    @DeleteMapping("/{id}")
    public ResponseEntity<GuestResponse> deleteGuest(@PathVariable Long id) {
        return ResponseEntity.ok(guestService.deleteGuest(id));
    }

    // PATCH /api/guests/5/enable
    @PatchMapping("/{id}/enable")
    public ResponseEntity<GuestResponse> enableGuest(@PathVariable Long id) {
        return ResponseEntity.ok(guestService.enableGuest(id));
    }
}