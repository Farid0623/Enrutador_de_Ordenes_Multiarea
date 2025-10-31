/**
 * Temporizador - E7: Timer con Reglas SLA
 * Sistema de Gestión de Órdenes
 *
 * Implementación FRONTEND:
 * - Ejecuta tick cada N_SEG segundos
 * - Llama a API POST /tick o actualiza localmente
 * - Incrementa seg_acumulados en asignaciones EN_PROGRESO/PENDIENTE
 * - Aplica estado_timeout si excede SLA_SEG
 * - Recalcula estado_global
 */

class Temporizador {
    constructor() {
        this.intervalId = null;
        this.isRunning = false;
        this.config = CONFIG.TEMPORIZADOR;
        this.tickCount = 0;

        if (this.config.AUTO_START) {
            this.start();
        }
    }

    /**
     * Iniciar temporizador
     */
    start() {
        if (this.isRunning) {
            log('Temporizador ya está ejecutándose');
            return;
        }

        if (!this.config.ENABLED) {
            log('Temporizador deshabilitado en configuración');
            return;
        }

        log(`Iniciando temporizador (cada ${this.config.N_SEG}s, SLA: ${this.config.SLA_SEG}s)`);

        this.isRunning = true;

        // Ejecutar primer tick inmediatamente
        this.tick();

        // Configurar intervalo
        this.intervalId = setInterval(() => {
            this.tick();
        }, this.config.N_SEG * 1000);
    }

    /**
     * Detener temporizador
     */
    stop() {
        if (!this.isRunning) {
            log('Temporizador ya está detenido');
            return;
        }

        log('Deteniendo temporizador');

        clearInterval(this.intervalId);
        this.intervalId = null;
        this.isRunning = false;
    }

    /**
     * Ejecutar un tick del temporizador
     */
    async tick() {
        this.tickCount++;
        log(`[Tick #${this.tickCount}] Ejecutando temporizador...`);

        try {
            // Opción A: Llamar a API backend POST /tick
            if (!CONFIG.FEATURES.ENABLE_MOCK_DATA) {
                await api.executeTick();
                log('[Tick] Ejecutado en backend');
                return;
            }

            // Opción B: Ejecutar lógica localmente (mock)
            await this.executeLocalTick();

        } catch (error) {
            logError('[Tick] Error:', error);
        }
    }

    /**
     * Ejecutar tick localmente (mock para demo)
     */
    async executeLocalTick() {
        // En una implementación real, esto sería manejado por el backend
        // Aquí simulamos el comportamiento para la demo

        log('[Tick Local] Incrementando tiempos y verificando SLA...');

        // Esta es una simulación - en producción el backend haría:
        // 1. Obtener todas las asignaciones activas (EN_PROGRESO, PENDIENTE)
        // 2. Incrementar seg_acumulados += N_SEG
        // 3. Si seg_acumulados >= SLA_SEG, cambiar a estado_timeout
        // 4. Recalcular estado_global de cada orden
        // 5. Registrar cambios en historial

        const estadosActivos = ['EN_PROGRESO', 'PENDIENTE'];
        const nSeg = this.config.N_SEG;
        const slaSeg = this.config.SLA_SEG;
        const estadoTimeout = this.config.ESTADO_TIMEOUT;

        log(`[Tick] Parámetros: N_SEG=${nSeg}, SLA_SEG=${slaSeg}, TIMEOUT=${estadoTimeout}`);

        // Simulación de eventos que ocurrirían en el backend
        const eventos = [
            'Incrementando tiempos en asignaciones activas',
            `Verificando SLA (límite: ${slaSeg}s)`,
            'Recalculando estados globales',
        ];

        eventos.forEach(evento => log(`[Tick] ${evento}`));

        // Emitir evento personalizado para que otros componentes puedan reaccionar
        this.emitTickEvent({
            timestamp: new Date().toISOString(),
            tickCount: this.tickCount,
            nSeg: nSeg,
            slaSeg: slaSeg,
        });
    }

    /**
     * Forzar ejecución de tick (para testing)
     */
    forceTick() {
        log('[Tick Forzado] Ejecutando...');
        this.tick();
    }

    /**
     * Emitir evento de tick
     */
    emitTickEvent(data) {
        const event = new CustomEvent('temporizador-tick', { detail: data });
        window.dispatchEvent(event);
    }

    /**
     * Obtener estado del temporizador
     */
    getStatus() {
        return {
            isRunning: this.isRunning,
            tickCount: this.tickCount,
            config: this.config,
        };
    }

    /**
     * Actualizar configuración
     */
    updateConfig(newConfig) {
        this.config = { ...this.config, ...newConfig };
        log('Configuración actualizada:', this.config);

        // Reiniciar si está corriendo
        if (this.isRunning) {
            this.stop();
            this.start();
        }
    }
}

/**
 * Backend Implementation Reference (Java)
 *
 * Este es el código que debería implementarse en el backend:
 *
 * @Scheduled(fixedDelayString = "${timer.interval.sec}000")
 * public void executeTick() {
 *     log.info("Ejecutando tick del temporizador...");
 *
 *     // 1. Obtener asignaciones activas
 *     List<AsignacionArea> asignacionesActivas = asignacionRepository
 *         .findByEstadoParcialIn(Arrays.asList(
 *             EstadoParcial.EN_PROGRESO,
 *             EstadoParcial.PENDIENTE
 *         ));
 *
 *     int nSeg = timerConfig.getNSeg();
 *     int slaSeg = timerConfig.getSLASeg();
 *     EstadoParcial estadoTimeout = timerConfig.getEstadoTimeout();
 *
 *     for (AsignacionArea asignacion : asignacionesActivas) {
 *         // 2. Incrementar tiempo
 *         asignacion.setSegAcumulados(
 *             asignacion.getSegAcumulados() + nSeg
 *         );
 *
 *         // 3. Verificar SLA
 *         if (asignacion.getEstadoParcial() == EstadoParcial.EN_PROGRESO &&
 *             asignacion.getSegAcumulados() >= slaSeg) {
 *
 *             // Cambiar a estado timeout
 *             EstadoParcial estadoAnterior = asignacion.getEstadoParcial();
 *             asignacion.setEstadoParcial(estadoTimeout);
 *
 *             // Registrar en historial
 *             historialService.registrar(
 *                 asignacion.getOrdenId(),
 *                 "SLA excedido - Cambio automático",
 *                 "SISTEMA",
 *                 estadoAnterior,
 *                 estadoTimeout
 *             );
 *
 *             log.warn("SLA excedido en orden {} área {}",
 *                 asignacion.getOrdenId(),
 *                 asignacion.getAreaId()
 *             );
 *         }
 *
 *         asignacionRepository.save(asignacion);
 *     }
 *
 *     // 4. Recalcular estados globales
 *     Set<String> ordenesAfectadas = asignacionesActivas.stream()
 *         .map(AsignacionArea::getOrdenId)
 *         .collect(Collectors.toSet());
 *
 *     for (String ordenId : ordenesAfectadas) {
 *         estadoGlobalService.recalcularEstadoGlobal(ordenId);
 *     }
 *
 *     log.info("Tick completado: {} asignaciones procesadas",
 *         asignacionesActivas.size()
 *     );
 * }
 */

// Inicializar temporizador globalmente
window.temporizador = new Temporizador();

// Listener para eventos de tick (para debugging o UI updates)
window.addEventListener('temporizador-tick', (event) => {
    log('[Temporizador Event]', event.detail);
});

// Exponer controles en consola para debugging
window.temporizadorControls = {
    start: () => window.temporizador.start(),
    stop: () => window.temporizador.stop(),
    tick: () => window.temporizador.forceTick(),
    status: () => window.temporizador.getStatus(),
    config: (newConfig) => window.temporizador.updateConfig(newConfig),
};

log('Temporizador inicializado. Controles disponibles en window.temporizadorControls');

