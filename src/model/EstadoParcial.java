package model;

/**
 * Estados parciales que puede tener una asignación de área en una orden
 */
public enum EstadoParcial {
    NUEVA,
    ASIGNADA,
    EN_PROGRESO,
    PENDIENTE,
    COMPLETADA,
    CERRADA_SIN_SOLUCION,
    VENCIDA;

    @Override
    public String toString() {
        return name().replace('_', ' ');
    }
}

