package model;

import java.time.LocalDateTime;

/**
 * Representa la asignación de una orden a un área específica con su estado parcial
 */
public class AsignacionArea {
    private final Area area;
    private Usuario agenteAsignado;
    private EstadoParcial estado;
    private final LocalDateTime fechaAsignacion;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaCompletada;
    private long tiempoTranscurridoSegundos;

    public AsignacionArea(Area area) {
        this.area = area;
        this.estado = EstadoParcial.ASIGNADA;
        this.fechaAsignacion = LocalDateTime.now();
        this.tiempoTranscurridoSegundos = 0;
    }

    public Area getArea() {
        return area;
    }

    public Usuario getAgenteAsignado() {
        return agenteAsignado;
    }

    public void setAgenteAsignado(Usuario agenteAsignado) {
        this.agenteAsignado = agenteAsignado;
    }

    public EstadoParcial getEstado() {
        return estado;
    }

    public void setEstado(EstadoParcial estado) {
        this.estado = estado;
        if (estado == EstadoParcial.EN_PROGRESO && fechaInicio == null) {
            fechaInicio = LocalDateTime.now();
        }
        if ((estado == EstadoParcial.COMPLETADA || estado == EstadoParcial.CERRADA_SIN_SOLUCION)
            && fechaCompletada == null) {
            fechaCompletada = LocalDateTime.now();
        }
    }

    public LocalDateTime getFechaAsignacion() {
        return fechaAsignacion;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public LocalDateTime getFechaCompletada() {
        return fechaCompletada;
    }

    public long getTiempoTranscurridoSegundos() {
        return tiempoTranscurridoSegundos;
    }

    public void incrementarTiempo(long segundos) {
        if (estado != EstadoParcial.COMPLETADA && estado != EstadoParcial.CERRADA_SIN_SOLUCION) {
            this.tiempoTranscurridoSegundos += segundos;
        }
    }

    public boolean estaActiva() {
        return estado != EstadoParcial.COMPLETADA && estado != EstadoParcial.CERRADA_SIN_SOLUCION;
    }

    @Override
    public String toString() {
        return area.getNombre() + ": " + estado +
               " (" + tiempoTranscurridoSegundos + "s)" +
               (agenteAsignado != null ? " - " + agenteAsignado.getNombre() : "");
    }
}

