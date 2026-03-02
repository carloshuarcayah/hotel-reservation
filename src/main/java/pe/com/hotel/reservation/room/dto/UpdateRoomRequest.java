package pe.com.hotel.reservation.room.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import pe.com.hotel.reservation.room.RoomStatus;
import pe.com.hotel.reservation.room.RoomType;

import java.math.BigDecimal;

public record UpdateRoomRequest(
        RoomType roomType,

        @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
        BigDecimal pricePerNight,

        @Min(value = 1, message = "La capacidad mínima es 1")
        @Max(value = 10, message = "La capacidad máxima es 10")
        Integer capacity,

        Integer floor,

        @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
        String description
) {
}
