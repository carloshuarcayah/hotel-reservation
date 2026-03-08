package pe.com.hotel.reservation.room;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.com.hotel.reservation.room.dto.CreateRoomRequest;
import pe.com.hotel.reservation.room.dto.RoomResponse;
import pe.com.hotel.reservation.room.dto.UpdateRoomRequest;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomService {
    private final RoomRepository roomRepository;

    public Page<RoomResponse> getAllRooms(Pageable pageable) {
        return roomRepository.findAll(pageable).map(RoomResponse::fromEntity);
    }

    public Page<RoomResponse> getActiveRooms(Pageable pageable) {
        return roomRepository.findByActiveTrue(pageable).map(RoomResponse::fromEntity);
    }

    public RoomResponse getRoomById(Long id) {
        Room room = findRoomOrThrow(id);
        return RoomResponse.fromEntity(room);
    }

    public Page<RoomResponse> getRoomsByType(RoomType roomType,Pageable pageable) {
        return roomRepository.findByRoomType(roomType,pageable)
                .map(RoomResponse::fromEntity);
    }

    public Page<RoomResponse> getRoomsByStatus(RoomStatus roomStatus,Pageable pageable){
        return roomRepository.findByStatus(roomStatus,pageable).map(RoomResponse::fromEntity);
    }

    public Page<RoomResponse> getAvailableRooms(RoomType roomType, LocalDate checkIn, LocalDate checkOut,Pageable pageable) {
        if (checkOut.isBefore(checkIn) || checkOut.isEqual(checkIn)) {
            throw new IllegalArgumentException("Date must be valid.");
        }
        return roomRepository.findAvailableRooms(roomType, checkIn, checkOut,pageable)
                .map(RoomResponse::fromEntity);
    }

    @Transactional
    public RoomResponse createRoom(CreateRoomRequest request) {
        if (roomRepository.existsByRoomNumber(request.roomNumber())) {
            throw new IllegalArgumentException(
                    "Already exists a room with number: " + request.roomNumber()
            );
        }

        Room room = Room.builder()
                .roomNumber(request.roomNumber())
                .roomType(request.roomType())
                .pricePerNight(request.pricePerNight())
                .capacity(request.capacity())
                .floor(request.floor())
                .description(request.description())
                .status(RoomStatus.AVAILABLE)
                .build();

        return RoomResponse.fromEntity(roomRepository.save(room));
    }

    @Transactional
    public RoomResponse updateRoom(Long id, UpdateRoomRequest request) {
        Room room = findRoomOrThrow(id);

        if (request.roomType() != null) room.setRoomType(request.roomType());
        if (request.pricePerNight() != null) room.setPricePerNight(request.pricePerNight());
        if (request.capacity() != null) room.setCapacity(request.capacity());
        if (request.floor() != null) room.setFloor(request.floor());
        if (request.description() != null) room.setDescription(request.description());

        return RoomResponse.fromEntity( roomRepository.save(room));
    }

    @Transactional
    public RoomResponse deleteRoom(Long id) {
        Room room = findRoomOrThrow(id);
        //todo: verificar que no eliminemos una habitacion que tiene reservas
        room.setActive(false);

        return RoomResponse.fromEntity(room);
    }

    @Transactional
    public RoomResponse enableRoom(Long id) {
        Room room = findRoomOrThrow(id);
        return RoomResponse.fromEntity(room);
    }

    //HELPERS
    private Room findRoomOrThrow(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Room not found with id: " + id));
    }
}
