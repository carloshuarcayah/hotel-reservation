package pe.com.hotel.reservation.guest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UpdateGuestRequest(
        @Size(max = 60, message = "First name must be 60 characters or less")
        String firstName,

        @Size(max = 60, message = "Last name must be 60 characters or less")
        String lastName,

        @Email(message = "Email must be valid")
        @Size(max = 100, message = "Email must be 100 characters or less")
        String email,

        @Size(max = 20, message = "Phone must be 20 characters or less")
        String phone
) {
}
