package config;

import model.EstadoParcial;

/**
 * Parámetros configurables del sistema
 */
public class Parametros {
    private int nSeg; // Intervalo de actualización del temporizador
    private int slaSeg; // SLA en segundos
    private EstadoParcial estadoTimeout; // Estado al que pasa cuando vence el timeout

    public Parametros(int nSeg, int slaSeg, EstadoParcial estadoTimeout) {
        this.nSeg = nSeg;
        this.slaSeg = slaSeg;
        this.estadoTimeout = estadoTimeout;
    }

    // Constructor con valores por defecto
    public Parametros() {
        this(15, 45, EstadoParcial.VENCIDA);
    }

    public int getNSeg() {
        return nSeg;
    }

    public void setNSeg(int nSeg) {
        if (nSeg > 0) {
            this.nSeg = nSeg;
        }
    }

    public int getSlaSeg() {
        return slaSeg;
    }

    public void setSlaSeg(int slaSeg) {
        if (slaSeg > 0) {
            this.slaSeg = slaSeg;
        }
    }

    public EstadoParcial getEstadoTimeout() {
        return estadoTimeout;
    }

    public void setEstadoTimeout(EstadoParcial estadoTimeout) {
        this.estadoTimeout = estadoTimeout;
    }

    @Override
    public String toString() {
        return String.format("N_seg=%ds, SLA_seg=%ds, Estado_timeout=%s",
                            nSeg, slaSeg, estadoTimeout);
    }
}

