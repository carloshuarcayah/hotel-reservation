package pe.com.hotel.reservation.reservation;

public enum ReservationStatus {
    PENDING,      // Recién creada, esperando confirmación (o pago en Fase 2)
    CONFIRMED,    // Confirmada, la habitación está asegurada
    CANCELLED,    // Cancelada por el huésped o el hotel
    COMPLETED     // El huésped ya hizo check-out, la estadía terminó
}