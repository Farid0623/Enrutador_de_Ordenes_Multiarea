package model;

/**
 * Estado global de una orden, calculado a partir de los estados parciales
 */
public enum EstadoGlobal {
    NUEVA,
    EN_PROCESO,
    COMPLETADA,
    CERRADA_SIN_SOLUCION,
    PARCIALMENTE_VENCIDA,
    TOTALMENTE_VENCIDA;

    @Override
    public String toString() {
        return name().replace('_', ' ');
    }
}

