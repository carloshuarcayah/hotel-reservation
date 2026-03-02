package pe.com.hotel.reservation.room.dto;

import jakarta.validation.constraints.*;
import pe.com.hotel.reservation.room.RoomType;

import java.math.BigDecimal;

public record CreateRoomRequest(
        @NotBlank(message = "Room number is required")
        // @NotBlank = no puede ser null NI vacío NI solo espacios
        String roomNumber,

        @NotNull(message = "Room type is required")
        RoomType roomType,

        @NotNull(message = "Price per night is required")
        @DecimalMin(value = "10.00", message = "Price must be greater than 10")
        BigDecimal pricePerNight,

        @NotNull(message = "Capacity is required")
        @Min(value = 1, message = "Capacity must be at least 1")
        @Max(value = 10, message = "Capacity must not exceed 10")
        Integer capacity,


        @NotNull(message = "Floor number is required")
        @Min(value = 1, message = "Floor must be at least 1")
        Integer floor,

        @Size(max = 500, message = "Description must be 500 characters or less")
        String description
) {
}
