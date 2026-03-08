package pe.com.hotel.reservation.reservation.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CreateReservationRequest(
        @NotNull(message = "Room ID is required")
        Long roomId,

        @NotNull(message = "Guest ID is required")
        Long guestId,

        @NotNull(message = "Check-in date is required")
        @FutureOrPresent(message = "Check-in date must be today or in the future")
        LocalDate checkInDate,

        @NotNull(message = "Check-out date is required")
        @Future(message = "Check-out date must be in the future")
        LocalDate checkOutDate,

        @Size(max = 500, message = "Notes must be 500 characters or less")
        String notes
) {
}
