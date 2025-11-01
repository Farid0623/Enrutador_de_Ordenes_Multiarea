/**
 * Dashboard - E5: Vista Lista + Filtros + KPIs
 * Sistema de Gestión de Órdenes
 */

class Dashboard {
    constructor() {
        this.ordenes = [];
        this.filteredOrdenes = [];
        this.currentFilters = {
            estado_global: '',
            prioridad: '',
            buscar: '',
        };
        this.kpis = null;

        this.init();
    }

    /**
     * Inicializar dashboard
     */
    async init() {
        log('Inicializando Dashboard...');

        this.setupEventListeners();
        await this.loadData();
        this.setupAutoRefresh();

        log('Dashboard inicializado');
    }

    /**
     * Configurar event listeners
     */
    setupEventListeners() {
        // Filtros
        const filterEstado = document.getElementById('filter-estado');
        const filterPrioridad = document.getElementById('filter-prioridad');
        const filterBuscar = document.getElementById('filter-buscar');
        const btnLimpiar = document.getElementById('btn-limpiar-filtros');

        if (filterEstado) {
            filterEstado.addEventListener('change', (e) => {
                this.currentFilters.estado_global = e.target.value;
                this.applyFilters();
            });
        }

        if (filterPrioridad) {
            filterPrioridad.addEventListener('change', (e) => {
                this.currentFilters.prioridad = e.target.value;
                this.applyFilters();
            });
        }

        if (filterBuscar) {
            let debounceTimer;
            filterBuscar.addEventListener('input', (e) => {
                clearTimeout(debounceTimer);
                debounceTimer = setTimeout(() => {
                    this.currentFilters.buscar = e.target.value.toLowerCase();
                    this.applyFilters();
                }, CONFIG.UI.DEBOUNCE_DELAY);
            });
        }

        if (btnLimpiar) {
            btnLimpiar.addEventListener('click', () => this.clearFilters());
        }

        // Botón nueva orden
        const btnNuevaOrden = document.getElementById('btn-nueva-orden');
        if (btnNuevaOrden) {
            btnNuevaOrden.addEventListener('click', () => this.showCreateOrderModal());
        }
    }

    /**
     * Cargar datos desde la API
     */
    async loadData() {
        try {
            this.showLoading();

            // Cargar órdenes y KPIs en paralelo
            const [ordenesRes, kpisRes] = await Promise.all([
                api.getOrdenes(),
                api.getKPIs(),
            ]);

            if (ordenesRes.success) {
                this.ordenes = ordenesRes.data;
                this.filteredOrdenes = [...this.ordenes];
            }

            if (kpisRes.success) {
                this.kpis = kpisRes.data.ordenes;
            }

            this.renderKPIs();
            this.renderOrdenes();
            this.updateLastUpdate();

            log('Datos cargados:', { ordenes: this.ordenes.length, kpis: this.kpis });

        } catch (error) {
            logError('Error cargando datos:', error);
            this.showError('Error al cargar los datos. Por favor, inténtelo de nuevo.');
        }
    }

    /**
     * Aplicar filtros
     */
    applyFilters() {
        log('Aplicando filtros:', this.currentFilters);

        this.filteredOrdenes = this.ordenes.filter(orden => {
            // Filtro por estado
            if (this.currentFilters.estado_global &&
                orden.estado_global !== this.currentFilters.estado_global) {
                return false;
            }

            // Filtro por prioridad
            if (this.currentFilters.prioridad &&
                orden.prioridad !== this.currentFilters.prioridad) {
                return false;
            }

            // Filtro por búsqueda
            if (this.currentFilters.buscar) {
                const searchTerm = this.currentFilters.buscar;
                const matchId = orden.id.toLowerCase().includes(searchTerm);
                const matchTitulo = orden.titulo.toLowerCase().includes(searchTerm);
                if (!matchId && !matchTitulo) {
                    return false;
                }
            }

            return true;
        });

        this.renderOrdenes();
        this.updateFiltersInfo();
    }

    /**
     * Limpiar filtros
     */
    clearFilters() {
        this.currentFilters = {
            estado_global: '',
            prioridad: '',
            buscar: '',
        };

        document.getElementById('filter-estado').value = '';
        document.getElementById('filter-prioridad').value = '';
        document.getElementById('filter-buscar').value = '';

        this.filteredOrdenes = [...this.ordenes];
        this.renderOrdenes();
        this.updateFiltersInfo();
    }

    /**
     * Renderizar KPIs
     */
    renderKPIs() {
        if (!this.kpis) return;

        const total = this.kpis.total || 0;

        // Actualizar valores
        this.updateKPI('total', this.kpis.total);
        this.updateKPI('completadas', this.kpis.completadas, total);
        this.updateKPI('proceso', this.kpis.en_proceso, total);
        this.updateKPI('sin-solucion', this.kpis.cerradas_sin_solucion, total);
        this.updateKPI('vencidas',
            (this.kpis.parcialmente_vencidas || 0) + (this.kpis.totalmente_vencidas || 0),
            total
        );
        this.updateKPI('nuevas', this.kpis.nuevas, total);
    }

    /**
     * Actualizar KPI individual
     */
    updateKPI(id, value, total = null) {
        const valueElement = document.getElementById(`kpi-${id}`);
        const pctElement = document.getElementById(`kpi-${id}-pct`);

        if (valueElement) {
            valueElement.textContent = value || 0;
        }

        if (pctElement && total > 0) {
            const percentage = ((value / total) * 100).toFixed(1);
            pctElement.textContent = `${percentage}%`;
        }
    }

    /**
     * Renderizar lista de órdenes
     */
    renderOrdenes() {
        const container = document.getElementById('orders-container');
        const loadingState = document.getElementById('loading-state');
        const emptyState = document.getElementById('empty-state');

        if (!container) return;

        // Ocultar estados
        if (loadingState) loadingState.style.display = 'none';
        if (emptyState) emptyState.style.display = 'none';

        // Si no hay órdenes
        if (this.filteredOrdenes.length === 0) {
            container.style.display = 'none';
            if (emptyState) emptyState.style.display = 'block';
            return;
        }

        // Renderizar cards
        container.style.display = 'grid';
        container.innerHTML = this.filteredOrdenes
            .map(orden => this.createOrderCard(orden))
            .join('');

        // Agregar event listeners a las cards
        container.querySelectorAll('.order-card').forEach(card => {
            card.addEventListener('click', () => {
                const ordenId = card.dataset.ordenId;
                this.navigateToDetail(ordenId);
            });

            // Accesibilidad: permitir navegación con teclado
            card.addEventListener('keypress', (e) => {
                if (e.key === 'Enter' || e.key === ' ') {
                    e.preventDefault();
                    const ordenId = card.dataset.ordenId;
                    this.navigateToDetail(ordenId);
                }
            });
        });
    }

    /**
     * Crear HTML de una card de orden
     */
    createOrderCard(orden) {
        const estadoConfig = CONFIG.ESTADOS.GLOBAL[orden.estado_global] || {};
        const prioridadConfig = CONFIG.PRIORIDADES[orden.prioridad] || {};
        const fechaActualizacion = this.formatDate(orden.actualizada_en);

        return `
            <article 
                class="order-card estado-${orden.estado_global}" 
                data-orden-id="${orden.id}"
                tabindex="0"
                role="button"
                aria-label="Ver detalles de ${orden.titulo}">
                <div class="order-header">
                    <span class="order-id">${orden.id}</span>
                    <span class="order-priority priority-${orden.prioridad}">
                        ${prioridadConfig.icon || ''} ${orden.prioridad}
                    </span>
                </div>
                
                <h3 class="order-title">${this.escapeHtml(orden.titulo)}</h3>
                
                <div class="order-status status-${orden.estado_global}">
                    <span>${estadoConfig.icon || ''}</span>
                    <span>${estadoConfig.label || orden.estado_global}</span>
                </div>
                
                <div class="order-meta">
                    <div class="order-meta-item" title="Número de áreas asignadas">
                        <span>📍</span>
                        <span>${orden.num_areas || 0} área${orden.num_areas !== 1 ? 's' : ''}</span>
                    </div>
                    <div class="order-meta-item" title="Última actualización">
                        <span>🕒</span>
                        <span>${fechaActualizacion}</span>
                    </div>
                </div>
            </article>
        `;
    }

    /**
     * Actualizar información de filtros
     */
    updateFiltersInfo() {
        const infoElement = document.getElementById('filters-info');
        if (!infoElement) return;

        const total = this.ordenes.length;
        const filtered = this.filteredOrdenes.length;

        if (filtered === total) {
            infoElement.textContent = `Mostrando todas las órdenes (${total})`;
        } else {
            infoElement.textContent = `Mostrando ${filtered} de ${total} órdenes`;
        }
    }

    /**
     * Mostrar loading
     */
    showLoading() {
        const loadingState = document.getElementById('loading-state');
        const ordersContainer = document.getElementById('orders-container');
        const emptyState = document.getElementById('empty-state');

        if (loadingState) loadingState.style.display = 'block';
        if (ordersContainer) ordersContainer.style.display = 'none';
        if (emptyState) emptyState.style.display = 'none';
    }

    /**
     * Mostrar error
     */
    showError(message) {
        // TODO: Implementar sistema de notificaciones
        alert(message);
    }

    /**
     * Actualizar timestamp
     */
    updateLastUpdate() {
        const element = document.getElementById('last-update');
        if (element) {
            element.textContent = new Date().toLocaleString('es-ES');
        }
    }

    /**
     * Configurar auto-refresh
     */
    setupAutoRefresh() {
        if (!CONFIG.FEATURES.ENABLE_AUTO_REFRESH) return;

        setInterval(async () => {
            log('Auto-refresh: Recargando datos...');
            await this.loadData();
        }, CONFIG.UI.REFRESH_INTERVAL);
    }

    /**
     * Navegar a detalle de orden
     */
    navigateToDetail(ordenId) {
        log('Navegando a detalle:', ordenId);
        window.location.href = `detalle.html?id=${ordenId}`;
    }

    /**
     * Mostrar modal de crear orden
     */
    showCreateOrderModal() {
        // TODO: Implementar modal
        alert('Funcionalidad de crear orden en desarrollo');
    }

    /**
     * Formatear fecha
     */
    formatDate(dateString) {
        if (!dateString) return '-';

        const date = new Date(dateString);
        const now = new Date();
        const diffMs = now - date;
        const diffMins = Math.floor(diffMs / 60000);
        const diffHours = Math.floor(diffMs / 3600000);
        const diffDays = Math.floor(diffMs / 86400000);

        if (diffMins < 1) return 'Hace un momento';
        if (diffMins < 60) return `Hace ${diffMins} min`;
        if (diffHours < 24) return `Hace ${diffHours} h`;
        if (diffDays < 7) return `Hace ${diffDays} día${diffDays > 1 ? 's' : ''}`;

        return date.toLocaleDateString('es-ES');
    }

    /**
     * Escapar HTML
     */
    escapeHtml(text) {
        const div = document.createElement('div');
        div.textContent = text;
        return div.innerHTML;
    }
}

// Inicializar dashboard cuando el DOM esté listo
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', () => {
        window.dashboard = new Dashboard();
    });
} else {
    window.dashboard = new Dashboard();
}

