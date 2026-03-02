package pe.com.hotel.reservation.room;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table(name = "rooms")
@Entity
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@Builder
public class Room {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room_number",unique = true,nullable = false,length = 10)
    private String roomNumber;

    @Column(name = "room_type",nullable = false)
    private RoomType roomType;

    @Column(name = "price_per_night", nullable = false,precision = 10,scale = 2)
    private BigDecimal pricePerNight;

    @Column(nullable = false)
    private Integer capacity;

    @Column(nullable = false)
    private Integer floor;

    @Column(length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(length = 10,nullable = false)
    private RoomStatus status;

    @CreationTimestamp
    @Column(name="created_at",nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updated_at;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active=true;
}
