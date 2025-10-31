package model;

import java.time.LocalDateTime;

/**
 * Registro de cambio en el historial de una orden
 */
public class CambioHistorial {
    private final LocalDateTime fecha;
    private final String ordenId;
    private final String descripcion;
    private final Usuario usuario;
    private final String detalleAnterior;
    private final String detalleNuevo;

    public CambioHistorial(String ordenId, String descripcion, Usuario usuario,
                          String detalleAnterior, String detalleNuevo) {
        this.fecha = LocalDateTime.now();
        this.ordenId = ordenId;
        this.descripcion = descripcion;
        this.usuario = usuario;
        this.detalleAnterior = detalleAnterior;
        this.detalleNuevo = detalleNuevo;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public String getOrdenId() {
        return ordenId;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public String getDetalleAnterior() {
        return detalleAnterior;
    }

    public String getDetalleNuevo() {
        return detalleNuevo;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s - %s: %s → %s (Usuario: %s)",
                fecha, ordenId, descripcion, detalleAnterior, detalleNuevo,
                usuario != null ? usuario.getNombre() : "SISTEMA");
    }
}

