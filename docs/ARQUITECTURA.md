# Estructura del Proyecto

## 📁 Árbol de Directorios

```
actividad_1/
│
├── src/                          # Código fuente de la aplicación
│   ├── Main.java                 # Punto de entrada principal
│   │
│   ├── model/                    # Modelos de dominio
│   │   ├── Area.java             # Área de trabajo
│   │   ├── AsignacionArea.java   # Relación orden-área
│   │   ├── CambioHistorial.java  # Registro de cambios
│   │   ├── EstadoGlobal.java     # Estados de orden completa
│   │   ├── EstadoParcial.java    # Estados de asignación
│   │   ├── Orden.java            # Orden de trabajo principal
│   │   ├── Rol.java              # Roles del sistema
│   │   └── Usuario.java          # Usuario del sistema
│   │
│   ├── service/                  # Lógica de negocio
│   │   ├── EstadoGlobalService.java    # Cálculo de estados
│   │   ├── HistorialService.java       # Gestión de historial
│   │   ├── KPIService.java             # Métricas y KPIs
│   │   ├── OrdenService.java           # CRUD de órdenes
│   │   └── TemporizadorService.java    # Timer automático
│   │
│   ├── repository/               # Acceso a datos
│   │   └── CatalogoRepository.java     # Catálogo de usuarios/áreas
│   │
│   ├── config/                   # Configuración
│   │   └── Parametros.java       # Parámetros del sistema
│   │
│   └── ui/                       # Interfaz de usuario
│       └── ConsolaDemo.java      # Demo interactiva
│
├── tests/                        # Tests unitarios e integración
│   └── (próximamente)
│
├── db/                           # Base de datos
│   ├── migrations/               # Scripts de migración
│   │   └── (próximamente)
│   ├── scripts/                  # Scripts SQL auxiliares
│   │   └── (próximamente)
│   └── data/                     # Datos persistentes (gitignored)
│
├── docs/                         # Documentación adicional
│   ├── ARQUITECTURA.md           # Este archivo
│   ├── API.md                    # Documentación de API (futuro)
│   └── DEPLOYMENT.md             # Guía de despliegue (futuro)
│
├── logs/                         # Logs de la aplicación (gitignored)
│
├── out/                          # Archivos compilados (gitignored)
│
├── .env.example                  # Plantilla de variables de entorno
├── .env                          # Variables de entorno locales (gitignored)
├── .gitignore                    # Archivos ignorados por Git
├── LICENSE                       # Licencia MIT
├── README.md                     # Documentación principal
├── GUIA_USO.md                   # Guía de uso detallada
├── RESUMEN.md                    # Resumen ejecutivo
├── DIAGRAMAS.md                  # Diagramas del sistema
│
├── run.sh                        # Script de ejecución (Linux/Mac)
├── run-java17.sh                 # Script para Java 17+ (Linux/Mac)
└── run.bat                       # Script de ejecución (Windows)
```

## 📦 Descripción de Módulos

### `/src` - Código Fuente

#### `model/` - Capa de Dominio
Contiene las entidades del negocio y sus relaciones:
- **Orden**: Entidad principal que representa una orden de trabajo
- **AsignacionArea**: Relación many-to-many entre Orden y Area
- **Usuario**: Actores del sistema (Despachador, Agente, Admin)
- **Area**: Departamentos o áreas de trabajo
- **Estados**: Enums para estados parciales y globales
- **CambioHistorial**: Auditoría de cambios

#### `service/` - Capa de Negocio
Implementa la lógica de negocio:
- **OrdenService**: CRUD y operaciones sobre órdenes
- **TemporizadorService**: Timer automático con reglas de SLA
- **EstadoGlobalService**: Cálculo de estados según reglas
- **HistorialService**: Trazabilidad y auditoría
- **KPIService**: Métricas y reportes

#### `repository/` - Capa de Datos
Gestión de persistencia:
- **CatalogoRepository**: Gestión de usuarios y áreas (actualmente en memoria)

#### `config/` - Configuración
Parámetros del sistema:
- **Parametros**: Configuración de temporizador, SLA, etc.

#### `ui/` - Interfaz de Usuario
Interfaces de interacción:
- **ConsolaDemo**: Demo interactiva por consola

### `/tests` - Tests

Estructura para testing (a implementar):
```
tests/
├── unit/                # Tests unitarios
│   ├── model/
│   ├── service/
│   └── repository/
├── integration/         # Tests de integración
└── e2e/                # Tests end-to-end
```

### `/db` - Base de Datos

#### `migrations/`
Scripts de migración versionados:
```sql
-- V001__initial_schema.sql
-- V002__add_timestamps.sql
-- V003__add_indexes.sql
```

#### `scripts/`
Scripts SQL auxiliares:
```
scripts/
├── seed_demo_data.sql      # Datos de demo
├── cleanup.sql             # Limpieza de datos
└── reports/                # Queries de reportes
    ├── kpis_dashboard.sql
    └── order_history.sql
```

### `/docs` - Documentación

- **ARQUITECTURA.md**: Este archivo - estructura y diseño
- **API.md**: Documentación de endpoints REST (futuro)
- **DEPLOYMENT.md**: Guía de despliegue en diferentes entornos
- **CONTRIBUTING.md**: Guía para contribuidores

## 🏗️ Capas de Arquitectura

```
┌────────────────────────────────────────────────────────┐
│                    Presentation Layer                   │
│                   (ui/ConsolaDemo)                      │
└─────────────────────────┬──────────────────────────────┘
                          │
┌─────────────────────────▼──────────────────────────────┐
│                    Service Layer                        │
│  ┌──────────────┐  ┌──────────────┐  ┌─────────────┐  │
│  │OrdenService  │  │KPIService    │  │Temporizador │  │
│  └──────────────┘  └──────────────┘  └─────────────┘  │
│  ┌──────────────┐  ┌──────────────┐                   │
│  │HistorialSvc  │  │EstadoGlobal  │                   │
│  └──────────────┘  └──────────────┘                   │
└─────────────────────────┬──────────────────────────────┘
                          │
┌─────────────────────────▼──────────────────────────────┐
│                  Repository Layer                       │
│              (CatalogoRepository)                       │
└─────────────────────────┬──────────────────────────────┘
                          │
┌─────────────────────────▼──────────────────────────────┐
│                    Domain Layer                         │
│  (Orden, Usuario, Area, AsignacionArea, etc.)          │
└─────────────────────────────────────────────────────────┘
```

## 🔄 Flujo de Datos

```
Usuario → ConsolaDemo → Service → Repository → Model → DB
                          ↓
                     HistorialService (auditoría)
                          ↓
                     TemporizadorService (background)
```

## 📐 Patrones de Diseño Utilizados

### 1. **Service Layer Pattern**
Separación de lógica de negocio en servicios independientes.

### 2. **Repository Pattern**
Abstracción del acceso a datos.

### 3. **Observer Pattern**
Temporizador que observa y actualiza órdenes activas.

### 4. **Strategy Pattern**
Cálculo de estados globales según diferentes reglas.

### 5. **Facade Pattern**
ConsolaDemo como fachada que simplifica interacción con servicios.

## 🔐 Principios SOLID Aplicados

- **S**ingle Responsibility: Cada clase tiene una única responsabilidad
- **O**pen/Closed: Extensible sin modificar código existente
- **L**iskov Substitution: Subtipos intercambiables
- **I**nterface Segregation: Interfaces específicas
- **D**ependency Inversion: Dependencia de abstracciones

## 📈 Escalabilidad

### Horizontal
- Servicios independientes pueden separarse en microservicios
- Base de datos puede escalarse con réplicas

### Vertical
- Optimización de queries
- Cache de resultados frecuentes
- Pool de conexiones

## 🔮 Evolución Futura

### Fase 2: API REST
```
src/
├── controller/        # REST controllers
├── dto/              # Data Transfer Objects
└── exception/        # Exception handlers
```

### Fase 3: Frontend
```
frontend/
├── src/
│   ├── components/
│   ├── services/
│   └── views/
└── public/
```

### Fase 4: Microservicios
```
services/
├── orden-service/
├── usuario-service/
├── notificacion-service/
└── api-gateway/
```

## 📊 Convenciones de Código

### Nomenclatura
- **Clases**: PascalCase (ej: `OrdenService`)
- **Métodos**: camelCase (ej: `crearOrden()`)
- **Constantes**: UPPER_SNAKE_CASE (ej: `MAX_RETRIES`)
- **Variables**: camelCase (ej: `ordenId`)
- **Packages**: lowercase (ej: `service`)

### Documentación
- Todas las clases públicas tienen Javadoc
- Métodos complejos incluyen comentarios explicativos
- README actualizado con cada feature

### Testing
- Cobertura mínima: 70%
- Tests unitarios para lógica de negocio
- Tests de integración para flujos completos

---

**Última actualización**: 31 de Octubre, 2025

