package pe.com.hotel.reservation.guest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
public record CreateGuestRequest(
        @NotBlank(message = "First name is required")
        @Size(max = 60, message = "First name must be 60 characters or less")
        String firstName,

        @NotBlank(message = "Last name is required")
        @Size(max = 60, message = "Last name must be 60 characters or less")
        String lastName,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        @Size(max = 100, message = "Email must be 100 characters or less")
        String email,

        @Size(max = 20, message = "Phone must be 20 characters or less")
        String phone,

        @NotBlank(message = "Document is required")
        @Size(max = 20, message = "Document must be 20 characters or less")
        String documentNumber
) {
}