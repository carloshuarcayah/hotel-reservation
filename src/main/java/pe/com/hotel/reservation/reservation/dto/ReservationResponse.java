package pe.com.hotel.reservation.reservation.dto;

import pe.com.hotel.reservation.reservation.Reservation;
import pe.com.hotel.reservation.reservation.ReservationStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record ReservationResponse(
        Long id,
        Long roomId,
        String roomNumber,
        Long guestId,
        String guestFullName,
        LocalDate checkInDate,
        LocalDate checkOutDate,
        BigDecimal totalPrice,
        ReservationStatus status,
        String notes,
        Boolean active,
        LocalDateTime createdAt
) {
    public static ReservationResponse fromEntity(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getRoom().getId(),
                reservation.getRoom().getRoomNumber(),
                reservation.getGuest().getId(),
                reservation.getGuest().getFirstName() + " " + reservation.getGuest().getLastName(),
                reservation.getCheckInDate(),
                reservation.getCheckOutDate(),
                reservation.getTotalPrice(),
                reservation.getStatus(),
                reservation.getNotes(),
                reservation.getActive(),
                reservation.getCreatedAt()
        );
    }
}