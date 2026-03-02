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
// Combina @Controller + @ResponseBody
// Todos los métodos devuelven datos (JSON) directamente, no vistas HTML

@RequestMapping("/api/rooms")
// Prefijo de ruta para TODOS los endpoints de este controller
// Todas las rutas empiezan con /api/rooms

@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    // GET /api/rooms
    @GetMapping
    public ResponseEntity<Page<RoomResponse>> getAllRooms(Pageable pageable) {
        // ResponseEntity te da control total sobre:
        //   - El body (los datos)
        //   - El status HTTP (200, 201, 404, etc.)
        //   - Los headers
        return ResponseEntity.ok(roomService.getAllRooms(pageable));
        // .ok() = HTTP 200
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

//    // GET /api/rooms/available?type=DOUBLE&checkIn=2026-04-01&checkOut=2026-04-05
//    @GetMapping("/available")
//    public ResponseEntity<Page<RoomResponse>> getAvailableRooms(
//            @RequestParam RoomType type,
//            @RequestParam LocalDate checkIn,
//            @RequestParam LocalDate checkOut
//    ) {
//        // @RequestParam extrae los parámetros de la URL (?type=...&checkIn=...)
//        // Spring convierte automáticamente el String "2026-04-01" a LocalDate
//        return ResponseEntity.ok(roomService.getAvailableRooms(type, checkIn, checkOut));
//    }

    // POST /api/rooms
    @PostMapping
    public ResponseEntity<RoomResponse> createRoom(@Valid @RequestBody CreateRoomRequest request) {
        // @Valid → activa las validaciones del DTO (@NotBlank, @NotNull, etc.)
        //          Si alguna falla, Spring lanza MethodArgumentNotValidException
        //          que nuestro GlobalExceptionHandler capturará
        // @RequestBody → deserializa el JSON del body a CreateRoomRequest
        RoomResponse created = roomService.createRoom(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
        // HTTP 201 Created → indica que se creó un recurso nuevo
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
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return ResponseEntity.noContent().build();
    }
}
