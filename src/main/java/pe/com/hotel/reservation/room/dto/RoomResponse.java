package pe.com.hotel.reservation.room.dto;

import pe.com.hotel.reservation.room.Room;
import pe.com.hotel.reservation.room.RoomStatus;
import pe.com.hotel.reservation.room.RoomType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record RoomResponse(
        Long id,
        String roomNumber,
        RoomType roomType,
        BigDecimal pricePerNight,
        Integer capacity,
        Integer floor,
        String description,
        RoomStatus status,
        LocalDateTime createdAt
) {
    public static RoomResponse fromEntity(Room room) {
        return new RoomResponse(
                room.getId(),
                room.getRoomNumber(),
                room.getRoomType(),
                room.getPricePerNight(),
                room.getCapacity(),
                room.getFloor(),
                room.getDescription(),
                room.getStatus(),
                room.getCreatedAt()
        );
    }
}
