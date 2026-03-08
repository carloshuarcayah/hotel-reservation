package pe.com.hotel.reservation.reservation;

public enum ReservationStatus {
    PENDING,      // Recién creada, espera confirmación
    CONFIRMED,    // la habitación está asegurada
    CANCELLED,    // Cancelada por el huésped o el hotel
    COMPLETED     // LA estadia termino
}