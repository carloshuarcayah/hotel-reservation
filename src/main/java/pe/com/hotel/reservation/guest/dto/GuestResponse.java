package pe.com.hotel.reservation.guest.dto;

import pe.com.hotel.reservation.guest.Guest;
import java.time.LocalDateTime;

public record GuestResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phone,
        Boolean active,
        String documentNumber,
        LocalDateTime createdAt
) {
    public static GuestResponse fromEntity(Guest guest) {
        return new GuestResponse(
                guest.getId(),
                guest.getFirstName(),
                guest.getLastName(),
                guest.getEmail(),
                guest.getPhone(),
                guest.getActive(),
                guest.getDocumentNumber(),
                guest.getCreatedAt()
        );
    }
}