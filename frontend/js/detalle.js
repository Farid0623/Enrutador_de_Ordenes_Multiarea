/**
 * Detalle - E6: Vista Detalle con Acciones e Historial
 * Sistema de Gestión de Órdenes
 */

class DetalleOrden {
    constructor() {
        this.ordenId = null;
        this.orden = null;
        this.historial = [];
        this.pendingAction = null;

        this.init();
    }

    /**
     * Inicializar vista de detalle
     */
    async init() {
        log('Inicializando Vista Detalle...');

        // Obtener ID de la orden de la URL
        const urlParams = new URLSearchParams(window.location.search);
        this.ordenId = urlParams.get('id');

        if (!this.ordenId) {
            this.showError('No se especificó ID de orden');
            setTimeout(() => window.location.href = 'index.html', 2000);
            return;
        }

        this.setupEventListeners();
        await this.loadOrden();
        await this.loadHistorial();

        log('Vista Detalle inicializada');
    }

    /**
     * Configurar event listeners
     */
    setupEventListeners() {
        // Modal de confirmación
        const modalCancel = document.getElementById('modal-cancel');
        const modalConfirm = document.getElementById('modal-confirm');
        const modalBackdrop = document.getElementById('modal-backdrop');

        if (modalCancel) {
            modalCancel.addEventListener('click', () => this.hideModal());
        }

        if (modalConfirm) {
            modalConfirm.addEventListener('click', () => this.confirmAction());
        }

        if (modalBackdrop) {
            modalBackdrop.addEventListener('click', () => this.hideModal());
        }

        // Botón de refrescar historial
        const btnRefresh = document.getElementById('btn-refresh-historial');
        if (btnRefresh) {
            btnRefresh.addEventListener('click', () => this.loadHistorial());
        }

        // Tecla ESC para cerrar modal
        document.addEventListener('keydown', (e) => {
            if (e.key === 'Escape') {
                this.hideModal();
            }
        });
    }

    /**
     * Cargar orden desde API
     */
    async loadOrden() {
        try {
            const response = await api.getOrden(this.ordenId);

            if (response.success) {
                this.orden = response.data;
                this.renderOrden();
            } else {
                throw new Error('No se pudo cargar la orden');
            }

        } catch (error) {
            logError('Error cargando orden:', error);
            this.showError('Error al cargar los detalles de la orden');
        }
    }

    /**
     * Cargar historial desde API
     */
    async loadHistorial() {
        try {
            const response = await api.getHistorial(this.ordenId);

            if (response.success) {
                this.historial = response.data;
                this.renderHistorial();
            }

        } catch (error) {
            logError('Error cargando historial:', error);
            // No mostrar error, el historial es secundario
        }
    }

    /**
     * Renderizar orden
     */
    renderOrden() {
        if (!this.orden) return;

        // Ocultar loading
        const loadingState = document.getElementById('loading-state');
        const detailContainer = document.getElementById('orden-detail-container');
        if (loadingState) loadingState.style.display = 'none';
        if (detailContainer) detailContainer.style.display = 'block';

        // Breadcrumb
        document.getElementById('breadcrumb-orden').textContent = this.orden.id;

        // Header
        document.getElementById('orden-id').textContent = this.orden.id;
        document.getElementById('orden-titulo').textContent = this.orden.titulo;
        document.getElementById('orden-creador').textContent = this.orden.creador || '-';
        document.getElementById('orden-creada').textContent = this.formatDateLong(this.orden.creada_en);
        document.getElementById('orden-actualizada').textContent = this.formatDateLong(this.orden.actualizada_en);
        document.getElementById('orden-descripcion').textContent = this.orden.descripcion || 'Sin descripción';

        // Prioridad
        const prioridadEl = document.getElementById('orden-prioridad');
        const prioridadConfig = CONFIG.PRIORIDADES[this.orden.prioridad] || {};
        prioridadEl.textContent = `${prioridadConfig.icon || ''} ${this.orden.prioridad}`;
        prioridadEl.className = `orden-prioridad priority-${this.orden.prioridad}`;

        // Estado
        const estadoEl = document.getElementById('orden-estado');
        const estadoConfig = CONFIG.ESTADOS.GLOBAL[this.orden.estado_global] || {};
        estadoEl.textContent = `${estadoConfig.icon || ''} ${estadoConfig.label || this.orden.estado_global}`;
        estadoEl.className = `orden-estado-badge status-${this.orden.estado_global}`;

        // Asignaciones
        this.renderAsignaciones();
    }

    /**
     * Renderizar asignaciones
     */
    renderAsignaciones() {
        const container = document.getElementById('asignaciones-container');
        const countBadge = document.getElementById('asignaciones-count');

        if (!container) return;

        const asignaciones = this.orden.asignaciones || [];

        if (countBadge) {
            countBadge.textContent = asignaciones.length;
        }

        if (asignaciones.length === 0) {
            container.innerHTML = '<p style="color: var(--gray-500);">No hay asignaciones de área</p>';
            return;
        }

        container.innerHTML = asignaciones.map((asignacion, index) =>
            this.createAsignacionCard(asignacion, index)
        ).join('');

        // Agregar event listeners a botones de acción
        this.attachActionListeners();
    }

    /**
     * Crear HTML de card de asignación
     */
    createAsignacionCard(asignacion, index) {
        const estadoConfig = CONFIG.ESTADOS.PARCIAL[asignacion.estado_parcial] || {};
        const tiempoFormatted = this.formatTiempo(asignacion.seg_acumulados || 0);

        // Determinar botones disponibles según estado
        const botones = this.getAvailableActions(asignacion.estado_parcial);

        return `
            <div class="asignacion-card estado-${asignacion.estado_parcial}" data-index="${index}">
                <div class="asignacion-header">
                    <div class="asignacion-area">${asignacion.area.nombre}</div>
                    <span class="asignacion-estado estado-badge-${asignacion.estado_parcial}">
                        ${estadoConfig.label || asignacion.estado_parcial}
                    </span>
                </div>
                
                <div class="asignacion-info">
                    <div class="asignacion-info-item">
                        <span class="info-label">Responsable:</span>
                        <span class="info-value">${asignacion.asignada_a || 'Sin asignar'}</span>
                    </div>
                    <div class="asignacion-info-item">
                        <span class="info-label">Tiempo acumulado:</span>
                        <span class="tiempo-acumulado">
                            ⏱️ ${tiempoFormatted}
                        </span>
                    </div>
                    ${asignacion.fecha_inicio ? `
                    <div class="asignacion-info-item">
                        <span class="info-label">Iniciada:</span>
                        <span class="info-value">${this.formatDateShort(asignacion.fecha_inicio)}</span>
                    </div>
                    ` : ''}
                </div>
                
                <div class="asignacion-actions">
                    ${this.createActionButtons(botones, asignacion)}
                </div>
            </div>
        `;
    }

    /**
     * Obtener acciones disponibles según estado
     */
    getAvailableActions(estado) {
        const actions = {
            NUEVA: ['iniciar'],
            ASIGNADA: ['iniciar'],
            EN_PROGRESO: ['pausar', 'completar', 'cerrar'],
            PENDIENTE: ['iniciar', 'completar', 'cerrar'],
            COMPLETADA: [],
            CERRADA_SIN_SOLUCION: [],
            VENCIDA: ['completar', 'cerrar'],
        };

        return actions[estado] || [];
    }

    /**
     * Crear botones de acción
     */
    createActionButtons(actions, asignacion) {
        const buttons = {
            iniciar: `
                <button class="btn-action btn-action-iniciar" 
                        data-action="iniciar" 
                        data-area-id="${asignacion.area.id}"
                        aria-label="Iniciar trabajo en ${asignacion.area.nombre}">
                    ▶️ Iniciar
                </button>
            `,
            pausar: `
                <button class="btn-action btn-action-pausar" 
                        data-action="pausar" 
                        data-area-id="${asignacion.area.id}"
                        aria-label="Pausar trabajo en ${asignacion.area.nombre}">
                    ⏸️ Pausar
                </button>
            `,
            completar: `
                <button class="btn-action btn-action-completar" 
                        data-action="completar" 
                        data-area-id="${asignacion.area.id}"
                        aria-label="Completar trabajo en ${asignacion.area.nombre}">
                    ✅ Completar
                </button>
            `,
            cerrar: `
                <button class="btn-action btn-action-cerrar" 
                        data-action="cerrar" 
                        data-area-id="${asignacion.area.id}"
                        aria-label="Cerrar sin solución en ${asignacion.area.nombre}">
                    ❌ Cerrar sin Solución
                </button>
            `,
        };

        return actions.map(action => buttons[action] || '').join('');
    }

    /**
     * Adjuntar listeners a botones de acción
     */
    attachActionListeners() {
        const buttons = document.querySelectorAll('.btn-action');

        buttons.forEach(button => {
            button.addEventListener('click', (e) => {
                const action = button.dataset.action;
                const areaId = button.dataset.areaId;
                this.showConfirmModal(action, areaId);
            });
        });
    }

    /**
     * Mostrar modal de confirmación
     */
    showConfirmModal(action, areaId) {
        const modal = document.getElementById('modal-confirmacion');
        const modalTitle = document.getElementById('modal-title');
        const modalMessage = document.getElementById('modal-message');

        if (!modal) return;

        const messages = {
            iniciar: {
                title: 'Iniciar Trabajo',
                message: '¿Confirmar inicio de trabajo en esta área?',
            },
            pausar: {
                title: 'Pausar Trabajo',
                message: '¿Confirmar pausa de trabajo en esta área?',
            },
            completar: {
                title: 'Completar Trabajo',
                message: '¿Confirmar completado de trabajo en esta área? Esta acción no se puede deshacer.',
            },
            cerrar: {
                title: 'Cerrar sin Solución',
                message: '¿Confirmar cierre sin solución? Esta acción no se puede deshacer.',
            },
        };

        const config = messages[action] || messages.iniciar;

        modalTitle.textContent = config.title;
        modalMessage.textContent = config.message;

        // Guardar acción pendiente
        this.pendingAction = { action, areaId };

        // Mostrar modal
        modal.classList.add('active');
    }

    /**
     * Ocultar modal
     */
    hideModal() {
        const modal = document.getElementById('modal-confirmacion');
        if (modal) {
            modal.classList.remove('active');
        }
        this.pendingAction = null;
    }

    /**
     * Confirmar acción
     */
    async confirmAction() {
        if (!this.pendingAction) return;

        const { action, areaId } = this.pendingAction;

        this.hideModal();

        try {
            await this.executeAction(action, areaId);
            this.showToast('Acción ejecutada exitosamente', 'success');

            // Recargar datos
            await Promise.all([
                this.loadOrden(),
                this.loadHistorial(),
            ]);

        } catch (error) {
            logError('Error ejecutando acción:', error);
            this.showToast('Error al ejecutar la acción', 'error');
        }
    }

    /**
     * Ejecutar acción en API
     */
    async executeAction(action, areaId) {
        const stateMap = {
            iniciar: 'EN_PROGRESO',
            pausar: 'PENDIENTE',
            completar: 'COMPLETADA',
            cerrar: 'CERRADA_SIN_SOLUCION',
        };

        const nuevoEstado = stateMap[action];

        if (!nuevoEstado) {
            throw new Error('Acción no válida');
        }

        return await api.updateAsignacion(this.ordenId, areaId, {
            estado_parcial: nuevoEstado,
        });
    }

    /**
     * Renderizar historial
     */
    renderHistorial() {
        const container = document.getElementById('historial-container');

        if (!container) return;

        if (this.historial.length === 0) {
            container.innerHTML = '<p style="color: var(--gray-500);">No hay registros en el historial</p>';
            return;
        }

        // Ordenar por fecha descendente (más reciente primero)
        const sorted = [...this.historial].sort((a, b) =>
            new Date(b.timestamp) - new Date(a.timestamp)
        );

        container.innerHTML = sorted.map(item => this.createHistorialItem(item)).join('');
    }

    /**
     * Crear HTML de item de historial
     */
    createHistorialItem(item) {
        return `
            <div class="historial-item">
                <div class="historial-evento">${item.evento}</div>
                ${item.detalle ? `<div class="historial-detalle">${item.detalle}</div>` : ''}
                <div class="historial-meta">
                    <span>👤 ${item.actor || 'Sistema'}</span>
                    <span>🕐 ${this.formatDateLong(item.timestamp)}</span>
                </div>
            </div>
        `;
    }

    /**
     * Mostrar toast
     */
    showToast(message, type = 'success') {
        const toast = document.getElementById('toast');
        const toastMessage = document.getElementById('toast-message');

        if (!toast || !toastMessage) return;

        toastMessage.textContent = message;
        toast.className = `toast ${type} active`;

        setTimeout(() => {
            toast.classList.remove('active');
        }, 3000);
    }

    /**
     * Mostrar error
     */
    showError(message) {
        this.showToast(message, 'error');
    }

    /**
     * Formatear tiempo en segundos a formato legible
     */
    formatTiempo(segundos) {
        if (!segundos || segundos === 0) return '0h 0m';

        const horas = Math.floor(segundos / 3600);
        const minutos = Math.floor((segundos % 3600) / 60);
        const segs = segundos % 60;

        if (horas > 0) {
            return `${horas}h ${minutos}m`;
        }
        if (minutos > 0) {
            return `${minutos}m ${segs}s`;
        }
        return `${segs}s`;
    }

    /**
     * Formatear fecha (formato largo)
     */
    formatDateLong(dateString) {
        if (!dateString) return '-';
        const date = new Date(dateString);
        return date.toLocaleString('es-ES', {
            year: 'numeric',
            month: 'long',
            day: 'numeric',
            hour: '2-digit',
            minute: '2-digit',
        });
    }

    /**
     * Formatear fecha (formato corto)
     */
    formatDateShort(dateString) {
        if (!dateString) return '-';
        const date = new Date(dateString);
        return date.toLocaleString('es-ES', {
            month: 'short',
            day: 'numeric',
            hour: '2-digit',
            minute: '2-digit',
        });
    }
}

// Inicializar vista de detalle cuando el DOM esté listo
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', () => {
        window.detalleOrden = new DetalleOrden();
    });
} else {
    window.detalleOrden = new DetalleOrden();
}

