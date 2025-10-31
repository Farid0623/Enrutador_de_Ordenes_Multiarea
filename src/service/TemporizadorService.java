package service;

import config.Parametros;
import model.*;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Servicio de temporizador que incrementa tiempos y aplica reglas cada N segundos
 */
public class TemporizadorService {
    private final OrdenService ordenService;
    private final Parametros parametros;
    private final HistorialService historialService;
    private Timer timer;
    private boolean ejecutando;

    public TemporizadorService(OrdenService ordenService, Parametros parametros,
                               HistorialService historialService) {
        this.ordenService = ordenService;
        this.parametros = parametros;
        this.historialService = historialService;
        this.ejecutando = false;
    }

    /**
     * Iniciar el temporizador
     */
    public void iniciar() {
        if (ejecutando) {
            return;
        }

        timer = new Timer(true);
        ejecutando = true;

        long intervaloMs = parametros.getNSeg() * 1000L;

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                procesarTick();
            }
        }, intervaloMs, intervaloMs);

        System.out.println("⏱️  Temporizador iniciado (cada " + parametros.getNSeg() + "s)");
    }

    /**
     * Detener el temporizador
     */
    public void detener() {
        if (timer != null) {
            timer.cancel();
            ejecutando = false;
            System.out.println("⏱️  Temporizador detenido");
        }
    }

    /**
     * Verificar si está ejecutando
     */
    public boolean estaEjecutando() {
        return ejecutando;
    }

    /**
     * Procesar un tick del temporizador
     */
    private void procesarTick() {
        int nSeg = parametros.getNSeg();
        int slaSeg = parametros.getSlaSeg();
        EstadoParcial estadoTimeout = parametros.getEstadoTimeout();

        for (Orden orden : ordenService.obtenerOrdenesActivas()) {
            boolean huboVencimiento = false;

            for (AsignacionArea asignacion : orden.getAsignaciones()) {
                if (asignacion.estaActiva()) {
                    // Incrementar tiempo
                    asignacion.incrementarTiempo(nSeg);

                    // Verificar si excede SLA
                    if (asignacion.getTiempoTranscurridoSegundos() >= slaSeg
                        && asignacion.getEstado() != EstadoParcial.VENCIDA) {

                        EstadoParcial estadoAnterior = asignacion.getEstado();
                        asignacion.setEstado(estadoTimeout);

                        historialService.registrarCambio(
                            orden.getId(),
                            "SLA excedido en " + asignacion.getArea().getNombre(),
                            null,
                            estadoAnterior.toString(),
                            estadoTimeout.toString()
                        );

                        huboVencimiento = true;
                    }
                }
            }

            // Recalcular estado global si hubo cambios
            if (huboVencimiento) {
                ordenService.actualizarEstadoGlobal(orden);
            }
        }
    }

    /**
     * Forzar un tick manual (para testing)
     */
    public void forzarTick() {
        procesarTick();
    }
}

