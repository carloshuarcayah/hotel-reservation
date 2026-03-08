package pe.com.hotel.reservation.room;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.com.hotel.reservation.room.dto.CreateRoomRequest;
import pe.com.hotel.reservation.room.dto.RoomResponse;
import pe.com.hotel.reservation.room.dto.UpdateRoomRequest;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    // GET /apt/rooms
    @GetMapping
    public ResponseEntity<Page<RoomResponse>> getAllRooms(Pageable pageable) {
        return ResponseEntity.ok(roomService.getAllRooms(pageable));
    }

    // GET /api/rooms/5
    @GetMapping("/{id}")
    public ResponseEntity<RoomResponse> getRoomById(@PathVariable Long id) {
        // @PathVariable extrae el {id} de la URL
        return ResponseEntity.ok(roomService.getRoomById(id));
    }

    // GET /api/rooms/type/DOUBLE
    @GetMapping("/type/{roomType}")
    public ResponseEntity<Page<RoomResponse>> getRoomsByType(@PathVariable RoomType roomType, Pageable pageable) {
        return ResponseEntity.ok(roomService.getRoomsByType(roomType,pageable));
    }

    @GetMapping("/available")
    public ResponseEntity<Page<RoomResponse>> getAvailableRooms(
            @RequestParam RoomType type,
            @RequestParam LocalDate checkIn,
            @RequestParam LocalDate checkOut,Pageable pageable
    ) {
        return ResponseEntity.ok(roomService.getAvailableRooms(type, checkIn, checkOut,pageable));
    }
    // POST /api/rooms
    @PostMapping
    public ResponseEntity<RoomResponse> createRoom(@Valid @RequestBody CreateRoomRequest request) {
        RoomResponse created = roomService.createRoom(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // PUT /api/rooms/5
    @PutMapping("/{id}")
    public ResponseEntity<RoomResponse> updateRoom(
            @PathVariable Long id,
            @Valid @RequestBody UpdateRoomRequest request
    ) {
        return ResponseEntity.ok(roomService.updateRoom(id, request));
    }

    // DELETE /api/rooms/5
    @DeleteMapping("/{id}")
    public ResponseEntity<RoomResponse> deleteRoom(@PathVariable Long id) {
        return ResponseEntity.ok(roomService.deleteRoom(id));
    }

    @PatchMapping("/{id}/enable")
    public ResponseEntity<RoomResponse> enableRoom(@PathVariable Long id){
        return ResponseEntity.ok(roomService.enableRoom(id));
    }
}
