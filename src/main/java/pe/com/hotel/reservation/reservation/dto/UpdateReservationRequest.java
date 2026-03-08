package pe.com.hotel.reservation.reservation.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record UpdateReservationRequest(
        @FutureOrPresent(message = "Check-in date must be today or in the future")
        LocalDate checkInDate,

        @Future(message = "Check-out date must be in the future")
        LocalDate checkOutDate,

        @Size(max = 500, message = "Notes must be 500 characters or less")
        String notes
) {
}