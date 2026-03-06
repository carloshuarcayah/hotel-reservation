package pe.com.hotel.reservation.guest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GuestRepository extends JpaRepository<Guest, Long> {
    Page<Guest> findByActiveTrue(Pageable pageable);
    Optional<Guest> findByEmail(String email);
    Optional<Guest> findByDocumentNumber(String documentNumber);
    boolean existsByEmail(String email);
    boolean existsByDocumentNumber(String documentNumber);
}
