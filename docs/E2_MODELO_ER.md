# 📊 E2: Modelo de Datos - Diagrama ER

## Entidades y Relaciones

### Diagrama ER (Representación Textual)

```
┌─────────────────────────────┐
│         AREAS               │
├─────────────────────────────┤
│ PK  id (VARCHAR)            │
│     nombre (VARCHAR)         │
│     responsable (VARCHAR)    │
│     contacto (VARCHAR)       │
│     descripcion (TEXT)       │
│     activa (BOOLEAN)         │
│     creada_en (TIMESTAMP)    │
│     actualizada_en (TIMESTAMP)│
└───────────┬─────────────────┘
            │
            │ 1
            │
            │ N
            │
┌───────────▼─────────────────┐
│      ORDEN_AREA             │
├─────────────────────────────┤
│ PK  id (BIGINT AUTO)        │
│ FK  orden_id                │
│ FK  area_id                 │
│     asignada_a (VARCHAR)     │
│     estado_parcial (ENUM)    │
│     seg_acumulados (BIGINT)  │
│     fecha_asignacion (TS)    │
│     fecha_inicio (TS)        │
│     fecha_completada (TS)    │
│     actualizada_en (TS)      │
└───────────┬─────────────────┘
            │
            │ N
            │
            │ 1
            │
┌───────────▼─────────────────┐
│         ORDENES             │
├─────────────────────────────┤
│ PK  id (VARCHAR)            │
│     titulo (VARCHAR)         │
│     descripcion (TEXT)       │
│     creador (VARCHAR)        │
│     estado_global (ENUM)     │
│     prioridad (ENUM)         │
│     creada_en (TIMESTAMP)    │
│     actualizada_en (TIMESTAMP)│
│     cerrada_en (TIMESTAMP)   │
└───────────┬─────────────────┘
            │
            │ 1
            │
            │ N
            │
┌───────────▼─────────────────┐
│        HISTORIAL            │
├─────────────────────────────┤
│ PK  id (BIGINT AUTO)        │
│ FK  orden_id                │
│     evento (VARCHAR)         │
│     detalle (TEXT)           │
│     estado_global (ENUM)     │
│     estado_parcial (ENUM)    │
│     area_id (VARCHAR)        │
│     timestamp (TIMESTAMP)    │
│     actor (VARCHAR)          │
└─────────────────────────────┘
```

---

## Relaciones

### 1. AREAS ↔ ORDEN_AREA (1:N)
- Una **área** puede tener múltiples **asignaciones**
- Una **asignación** pertenece a una sola **área**
- **Clave foránea**: `orden_area.area_id → areas.id`
- **ON DELETE**: CASCADE (si se elimina área, se eliminan asignaciones)

### 2. ORDENES ↔ ORDEN_AREA (1:N)
- Una **orden** puede tener múltiples **asignaciones de área** (multiárea)
- Una **asignación** pertenece a una sola **orden**
- **Clave foránea**: `orden_area.orden_id → ordenes.id`
- **ON DELETE**: CASCADE (si se elimina orden, se eliminan asignaciones)
- **UNIQUE**: `(orden_id, area_id)` - No duplicar asignación

### 3. ORDENES ↔ HISTORIAL (1:N)
- Una **orden** tiene múltiples **registros de historial**
- Un **registro** pertenece a una sola **orden**
- **Clave foránea**: `historial.orden_id → ordenes.id`
- **ON DELETE**: CASCADE (si se elimina orden, se elimina historial)

---

## Enumeraciones (ENUMs)

### EstadoParcial (orden_area.estado_parcial)
```sql
'NUEVA', 'ASIGNADA', 'EN_PROGRESO', 'PENDIENTE', 
'COMPLETADA', 'CERRADA_SIN_SOLUCION', 'VENCIDA'
```

### EstadoGlobal (ordenes.estado_global, historial.estado_global)
```sql
'NUEVA', 'EN_PROCESO', 'COMPLETADA', 'CERRADA_SIN_SOLUCION',
'PARCIALMENTE_VENCIDA', 'TOTALMENTE_VENCIDA'
```

### Prioridad (ordenes.prioridad)
```sql
'BAJA', 'MEDIA', 'ALTA', 'CRITICA'
```

---

## Índices Estratégicos

### Tabla: AREAS
- `idx_areas_activa` - Filtrar áreas activas
- `idx_areas_responsable` - Búsqueda por responsable

### Tabla: ORDENES
- `idx_ordenes_estado_global` - Filtrar por estado (queries frecuentes)
- `idx_ordenes_creador` - Búsqueda por creador
- `idx_ordenes_creada_en` - Ordenamiento temporal
- `idx_ordenes_prioridad` - Filtrar por prioridad
- `idx_ordenes_estado_fecha` - Compuesto: (estado_global, creada_en)

### Tabla: ORDEN_AREA
- `idx_orden_area_orden` - Join con ordenes
- `idx_orden_area_area` - Join con areas
- `idx_orden_area_estado` - Filtrar por estado parcial
- `idx_orden_area_asignada_a` - Búsqueda por agente
- `idx_orden_area_activa` - Compuesto: (estado_parcial, seg_acumulados)

### Tabla: HISTORIAL
- `idx_historial_orden` - Historial por orden (query más frecuente)
- `idx_historial_timestamp` - Ordenamiento temporal
- `idx_historial_actor` - Auditoría por usuario
- `idx_historial_orden_timestamp` - Compuesto: (orden_id, timestamp)

---

## Tipos de Datos Elegidos

### Para H2 (Desarrollo)

| Campo | Tipo | Justificación |
|-------|------|---------------|
| `id` (areas, ordenes) | `VARCHAR(50)` | IDs legibles tipo "ORD-00001", "AREA-001" |
| `id` (orden_area, historial) | `BIGINT AUTO_INCREMENT` | Secuencial automático |
| `nombre`, `titulo` | `VARCHAR(500)` | Longitud razonable para títulos |
| `descripcion`, `detalle` | `TEXT` | Sin límite predefinido |
| `responsable`, `creador`, `actor` | `VARCHAR(255)` | Nombres de usuario |
| `contacto` | `VARCHAR(255)` | Email o teléfono |
| `seg_acumulados` | `BIGINT` | Segundos (hasta 292 mil millones de años) |
| `activa` | `BOOLEAN` | True/False simple |
| `estado_parcial`, `estado_global` | `VARCHAR(50)` con `CHECK` | ENUMs simulados |
| `prioridad` | `VARCHAR(20)` con `CHECK` | ENUM simulado |
| `timestamps` | `TIMESTAMP` | Precisión hasta milisegundos |

### Para PostgreSQL (Producción)

Cambios respecto a H2:
- ENUMs nativos en lugar de VARCHAR con CHECK
- `SERIAL` o `BIGSERIAL` en lugar de `AUTO_INCREMENT`
- `TIMESTAMPTZ` para timestamps con zona horaria
- `TEXT` sin distinción de VARCHAR (PostgreSQL optimiza automáticamente)

---

## Reglas de Integridad

### 1. Integridad Referencial
```sql
✅ FK orden_area.orden_id → ordenes.id (CASCADE)
✅ FK orden_area.area_id → areas.id (CASCADE)
✅ FK historial.orden_id → ordenes.id (CASCADE)
```

### 2. Integridad de Dominio
```sql
✅ CHECK estado_parcial IN ('NUEVA', 'ASIGNADA', ...)
✅ CHECK estado_global IN ('NUEVA', 'EN_PROCESO', ...)
✅ CHECK prioridad IN ('BAJA', 'MEDIA', 'ALTA', 'CRITICA')
✅ CHECK seg_acumulados >= 0
```

### 3. Integridad de Entidad
```sql
✅ PRIMARY KEY en todas las tablas
✅ NOT NULL en campos críticos (id, titulo, estado_global)
✅ UNIQUE (orden_id, area_id) en orden_area
```

### 4. Integridad de Negocio
```sql
✅ DEFAULT CURRENT_TIMESTAMP en timestamps
✅ DEFAULT 0 en seg_acumulados
✅ DEFAULT TRUE en activa
✅ DEFAULT 'MEDIA' en prioridad
```

---

## Diagrama ER Visual (Representación ASCII)

```
                    ┌──────────────┐
                    │    AREAS     │
                    │──────────────│
                    │ PK id        │
                    │    nombre    │
                    │    responsable│
                    └──────┬───────┘
                           │
                           │ 1:N
                           │
┌──────────────┐    ┌──────▼───────┐    ┌──────────────┐
│  HISTORIAL   │    │ ORDEN_AREA   │    │   ORDENES    │
│──────────────│    │──────────────│    │──────────────│
│ PK id        │    │ PK id        │    │ PK id        │
│ FK orden_id  │◄───│ FK orden_id  │◄───│    titulo    │
│    evento    │N:1 │ FK area_id   │1:N │    estado    │
│    detalle   │    │    estado    │    │    creador   │
│    timestamp │    │    seg_acum  │    └──────────────┘
└──────────────┘    └──────────────┘
```

---

## Cardinalidad Detallada

1. **AREAS → ORDEN_AREA**: 1:N
   - Mínimo: 0 (área sin asignaciones)
   - Máximo: N (área con múltiples asignaciones)

2. **ORDENES → ORDEN_AREA**: 1:N
   - Mínimo: 0 (orden nueva sin asignar)
   - Máximo: N (orden multiárea)

3. **ORDENES → HISTORIAL**: 1:N
   - Mínimo: 1 (al menos registro de creación)
   - Máximo: N (múltiples cambios)

---

## Escenarios de Uso

### Orden Simple (1 Área)
```
Orden-001 → [Asignación A1] → Área Técnica
          → [Historial H1, H2, H3]
```

### Orden Multiárea (3 Áreas)
```
Orden-002 → [Asignación A2] → Área Técnica
          → [Asignación A3] → Área Comercial
          → [Asignación A4] → Área Soporte
          → [Historial H4, H5, H6, H7, H8]
```

---

## Notas de Diseño

### ✅ Decisiones Clave

1. **IDs Legibles vs Auto-increment**
   - `ordenes.id` y `areas.id`: VARCHAR para IDs legibles (ORD-00001)
   - `orden_area.id` y `historial.id`: BIGINT auto para simplicidad

2. **ENUM vs VARCHAR**
   - H2: VARCHAR con CHECK (compatibilidad)
   - PostgreSQL: ENUM nativo (tipado fuerte)

3. **Timestamps**
   - `creada_en`, `actualizada_en`: Auditoría automática
   - `DEFAULT CURRENT_TIMESTAMP`: Automatización
   - `ON UPDATE CURRENT_TIMESTAMP`: No soportado en H2, manejado en aplicación

4. **Soft Delete**
   - `areas.activa`: Permite desactivar sin eliminar
   - `ordenes.cerrada_en`: Marca cierre sin eliminar registro

5. **Índices Compuestos**
   - `(estado_global, creada_en)`: Queries de dashboard
   - `(orden_id, timestamp)`: Historial cronológico
   - `(estado_parcial, seg_acumulados)`: Detección de vencidas

---

## Extensibilidad Futura

### Posibles Mejoras:
- Tabla `usuarios` separada (actualmente solo VARCHAR)
- Tabla `comentarios` para colaboración
- Tabla `adjuntos` para documentos
- Tabla `notificaciones` para alertas
- Tabla `auditoria` adicional a historial
- Campos `metadata` JSON para flexibilidad

---

**Última actualización**: 31 de Octubre, 2025  
**Base de datos**: H2 (desarrollo), PostgreSQL (producción)  
**Versión del modelo**: 1.0.0

