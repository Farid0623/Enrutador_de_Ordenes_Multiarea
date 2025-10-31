# 📋 E4: API REST - Documentación de Endpoints

## Sistema de Gestión de Órdenes - API v1

**Base URL**: `/api/v1`  
**Formato**: JSON  
**Autenticación**: Bearer Token (futuro) / Ninguna (MVP)  
**Versionado**: En URL (`/api/v1/`)

---

## 📑 Tabla de Contenidos

1. [Órdenes](#1-órdenes)
   - POST /ordenes
   - GET /ordenes
   - GET /ordenes/:id
   - PATCH /ordenes/:id
   - DELETE /ordenes/:id
2. [Asignaciones de Áreas](#2-asignaciones-de-áreas)
   - POST /ordenes/:id/asignaciones
   - DELETE /ordenes/:id/asignaciones/:areaId
   - PATCH /ordenes/:id/areas/:areaId
3. [Historial](#3-historial)
   - GET /ordenes/:id/historial
   - GET /historial
4. [Áreas](#4-áreas)
   - GET /areas
   - GET /areas/:id
5. [KPIs y Dashboard](#5-kpis-y-dashboard)
   - GET /kpis
   - GET /dashboard

---

## 1. Órdenes

### 1.1 Crear Orden

**Endpoint**: `POST /api/v1/ordenes`

**Descripción**: Crea una nueva orden de trabajo

**Request Body**:
```json
{
  "titulo": "string (requerido, max 500)",
  "descripcion": "string (opcional)",
  "creador": "string (requerido, max 255)",
  "prioridad": "enum (opcional: BAJA|MEDIA|ALTA|CRITICA, default: MEDIA)"
}
```

**Response 201 Created**:
```json
{
  "success": true,
  "data": {
    "id": "ORD-00009",
    "titulo": "Instalación CCTV Centro Comercial",
    "descripcion": "Sistema de 32 cámaras IP...",
    "creador": "Carlos Martínez",
    "estado_global": "NUEVA",
    "prioridad": "ALTA",
    "creada_en": "2025-10-31T10:30:00Z",
    "actualizada_en": "2025-10-31T10:30:00Z",
    "cerrada_en": null
  },
  "message": "Orden creada exitosamente"
}
```

**Errores**:
```json
// 400 Bad Request - Validación
{
  "success": false,
  "error": {
    "code": "VALIDATION_ERROR",
    "message": "Título es requerido",
    "details": {
      "field": "titulo",
      "constraint": "NOT_NULL"
    }
  }
}

// 422 Unprocessable Entity - Lógica de negocio
{
  "success": false,
  "error": {
    "code": "BUSINESS_RULE_VIOLATION",
    "message": "Prioridad inválida: debe ser BAJA, MEDIA, ALTA o CRITICA",
    "details": {
      "field": "prioridad",
      "provided": "SUPER_ALTA",
      "allowed": ["BAJA", "MEDIA", "ALTA", "CRITICA"]
    }
  }
}
```

**Ejemplos de uso**:
```bash
# cURL
curl -X POST http://localhost:8080/api/v1/ordenes \
  -H "Content-Type: application/json" \
  -d '{
    "titulo": "Reparación urgente alarmas",
    "descripcion": "Cliente VIP reporta fallo",
    "creador": "María Rodríguez",
    "prioridad": "CRITICA"
  }'

# HTTPie
http POST localhost:8080/api/v1/ordenes \
  titulo="Mantenimiento preventivo" \
  creador="Pedro Gómez" \
  prioridad=MEDIA
```

---

### 1.2 Listar Órdenes

**Endpoint**: `GET /api/v1/ordenes`

**Descripción**: Obtiene lista de órdenes con filtros y paginación

**Query Parameters**:
```
estado_global (opcional): NUEVA|EN_PROCESO|COMPLETADA|CERRADA_SIN_SOLUCION|PARCIALMENTE_VENCIDA|TOTALMENTE_VENCIDA
prioridad (opcional): BAJA|MEDIA|ALTA|CRITICA
creador (opcional): nombre del creador
desde (opcional): fecha ISO 8601
hasta (opcional): fecha ISO 8601
page (opcional): número de página (default: 1)
limit (opcional): registros por página (default: 20, max: 100)
sort (opcional): campo de ordenamiento (default: creada_en)
order (opcional): ASC|DESC (default: DESC)
```

**Response 200 OK**:
```json
{
  "success": true,
  "data": [
    {
      "id": "ORD-00008",
      "titulo": "Actualización firmware cámaras",
      "descripcion": "12 cámaras Hikvision...",
      "creador": "Ana López",
      "estado_global": "NUEVA",
      "prioridad": "BAJA",
      "num_areas": 1,
      "creada_en": "2025-10-31T09:00:00Z",
      "actualizada_en": "2025-10-31T09:00:00Z"
    },
    {
      "id": "ORD-00007",
      "titulo": "Mantenimiento preventivo trimestral",
      "descripcion": "8 tiendas cadena TopMart...",
      "creador": "Laura Fernández",
      "estado_global": "NUEVA",
      "prioridad": "BAJA",
      "num_areas": 3,
      "creada_en": "2025-10-31T08:30:00Z",
      "actualizada_en": "2025-10-31T08:30:00Z"
    }
  ],
  "pagination": {
    "page": 1,
    "limit": 20,
    "total": 8,
    "pages": 1
  },
  "filters": {
    "estado_global": null,
    "prioridad": null
  }
}
```

**Ejemplos**:
```bash
# Todas las órdenes
GET /api/v1/ordenes

# Órdenes en proceso con alta prioridad
GET /api/v1/ordenes?estado_global=EN_PROCESO&prioridad=ALTA

# Órdenes de un creador, paginadas
GET /api/v1/ordenes?creador=Carlos Martínez&page=1&limit=10

# Órdenes de últimos 3 días
GET /api/v1/ordenes?desde=2025-10-28T00:00:00Z&hasta=2025-10-31T23:59:59Z
```

---

### 1.3 Obtener Detalle de Orden

**Endpoint**: `GET /api/v1/ordenes/:id`

**Descripción**: Obtiene detalle completo de una orden incluyendo asignaciones

**Response 200 OK**:
```json
{
  "success": true,
  "data": {
    "id": "ORD-00005",
    "titulo": "Proyecto integral edificio corporativo",
    "descripcion": "Proyecto completo de seguridad...",
    "creador": "Carlos Martínez",
    "estado_global": "EN_PROCESO",
    "prioridad": "CRITICA",
    "creada_en": "2025-10-29T10:00:00Z",
    "actualizada_en": "2025-10-31T15:30:00Z",
    "cerrada_en": null,
    "asignaciones": [
      {
        "id": 12,
        "area": {
          "id": "AREA-COM",
          "nombre": "Área Comercial",
          "responsable": "Pedro Gómez Ruiz"
        },
        "asignada_a": "Pedro Gómez Ruiz",
        "estado_parcial": "COMPLETADA",
        "seg_acumulados": 172800,
        "fecha_asignacion": "2025-10-29T10:05:00Z",
        "fecha_inicio": "2025-10-29T11:00:00Z",
        "fecha_completada": "2025-10-31T11:00:00Z"
      },
      {
        "id": 13,
        "area": {
          "id": "AREA-LOG",
          "nombre": "Área Logística",
          "responsable": "Laura Fernández Torres"
        },
        "asignada_a": "Laura Fernández Torres",
        "estado_parcial": "EN_PROGRESO",
        "seg_acumulados": 86400,
        "fecha_asignacion": "2025-10-30T09:00:00Z",
        "fecha_inicio": "2025-10-30T10:00:00Z",
        "fecha_completada": null
      }
    ],
    "estadisticas": {
      "total_areas": 4,
      "completadas": 1,
      "en_progreso": 1,
      "pendientes": 2,
      "tiempo_total_segundos": 259200,
      "tiempo_total_horas": 72
    }
  }
}
```

**Errores**:
```json
// 404 Not Found
{
  "success": false,
  "error": {
    "code": "NOT_FOUND",
    "message": "Orden no encontrada",
    "details": {
      "resource": "orden",
      "id": "ORD-99999"
    }
  }
}
```

---

### 1.4 Actualizar Orden

**Endpoint**: `PATCH /api/v1/ordenes/:id`

**Descripción**: Actualiza campos de una orden

**Request Body** (todos opcionales):
```json
{
  "titulo": "string",
  "descripcion": "string",
  "prioridad": "enum (BAJA|MEDIA|ALTA|CRITICA)"
}
```

**Response 200 OK**:
```json
{
  "success": true,
  "data": {
    "id": "ORD-00005",
    "titulo": "Proyecto integral edificio corporativo - URGENTE",
    "prioridad": "CRITICA",
    "actualizada_en": "2025-10-31T16:00:00Z"
  },
  "message": "Orden actualizada exitosamente"
}
```

**Errores**:
```json
// 409 Conflict - Orden cerrada
{
  "success": false,
  "error": {
    "code": "CONFLICT",
    "message": "No se puede modificar una orden cerrada",
    "details": {
      "orden_id": "ORD-00003",
      "estado_actual": "COMPLETADA",
      "cerrada_en": "2025-10-30T18:00:00Z"
    }
  }
}
```

---

### 1.5 Eliminar Orden

**Endpoint**: `DELETE /api/v1/ordenes/:id`

**Descripción**: Elimina una orden (solo si está en estado NUEVA)

**Response 200 OK**:
```json
{
  "success": true,
  "message": "Orden eliminada exitosamente",
  "data": {
    "id": "ORD-00009",
    "titulo": "Orden eliminada"
  }
}
```

**Errores**:
```json
// 409 Conflict - No se puede eliminar
{
  "success": false,
  "error": {
    "code": "CONFLICT",
    "message": "Solo se pueden eliminar órdenes en estado NUEVA",
    "details": {
      "orden_id": "ORD-00005",
      "estado_actual": "EN_PROCESO",
      "tiene_asignaciones": true
    }
  }
}
```

---

## 2. Asignaciones de Áreas

### 2.1 Asignar Área a Orden

**Endpoint**: `POST /api/v1/ordenes/:id/asignaciones`

**Descripción**: Asigna una o más áreas a una orden

**Request Body**:
```json
{
  "area_id": "string (requerido)",
  "asignada_a": "string (opcional)"
}
```

**Response 201 Created**:
```json
{
  "success": true,
  "data": {
    "id": 25,
    "orden_id": "ORD-00008",
    "area": {
      "id": "AREA-TEC",
      "nombre": "Área Técnica"
    },
    "asignada_a": "Ana López García",
    "estado_parcial": "ASIGNADA",
    "seg_acumulados": 0,
    "fecha_asignacion": "2025-10-31T16:15:00Z"
  },
  "message": "Área asignada exitosamente"
}
```

**Errores**:
```json
// 409 Conflict - Área ya asignada
{
  "success": false,
  "error": {
    "code": "DUPLICATE_ASSIGNMENT",
    "message": "El área ya está asignada a esta orden",
    "details": {
      "orden_id": "ORD-00008",
      "area_id": "AREA-TEC",
      "asignacion_existente_id": 24
    }
  }
}

// 404 Not Found - Área no existe
{
  "success": false,
  "error": {
    "code": "NOT_FOUND",
    "message": "Área no encontrada",
    "details": {
      "resource": "area",
      "id": "AREA-XXX"
    }
  }
}
```

---

### 2.2 Quitar Área de Orden

**Endpoint**: `DELETE /api/v1/ordenes/:id/asignaciones/:areaId`

**Descripción**: Elimina la asignación de un área a una orden

**Response 200 OK**:
```json
{
  "success": true,
  "message": "Asignación eliminada exitosamente",
  "data": {
    "orden_id": "ORD-00008",
    "area_id": "AREA-CAL",
    "estado_previo": "NUEVA"
  }
}
```

**Errores**:
```json
// 409 Conflict - Asignación con trabajo iniciado
{
  "success": false,
  "error": {
    "code": "CONFLICT",
    "message": "No se puede eliminar asignación con trabajo iniciado",
    "details": {
      "area_id": "AREA-TEC",
      "estado_actual": "EN_PROGRESO",
      "seg_acumulados": 3600
    }
  }
}
```

---

### 2.3 Cambiar Estado de Asignación

**Endpoint**: `PATCH /api/v1/ordenes/:id/areas/:areaId`

**Descripción**: Cambia el estado parcial de una asignación de área

**Request Body**:
```json
{
  "estado_parcial": "enum (ASIGNADA|EN_PROGRESO|PENDIENTE|COMPLETADA|CERRADA_SIN_SOLUCION)",
  "asignada_a": "string (opcional)"
}
```

**Response 200 OK**:
```json
{
  "success": true,
  "data": {
    "id": 24,
    "orden_id": "ORD-00008",
    "area_id": "AREA-TEC",
    "estado_parcial": "EN_PROGRESO",
    "estado_anterior": "ASIGNADA",
    "fecha_inicio": "2025-10-31T16:20:00Z",
    "actualizada_en": "2025-10-31T16:20:00Z"
  },
  "message": "Estado actualizado exitosamente"
}
```

**Validaciones de transiciones**:
```json
// 422 Unprocessable Entity - Transición inválida
{
  "success": false,
  "error": {
    "code": "INVALID_STATE_TRANSITION",
    "message": "Transición de estado no permitida",
    "details": {
      "estado_actual": "COMPLETADA",
      "estado_solicitado": "EN_PROGRESO",
      "razon": "No se puede reabrir una asignación completada"
    }
  }
}
```

---

## 3. Historial

### 3.1 Obtener Historial de Orden

**Endpoint**: `GET /api/v1/ordenes/:id/historial`

**Descripción**: Obtiene el historial completo de cambios de una orden

**Query Parameters**:
```
limit (opcional): número de registros (default: 50)
offset (opcional): offset para paginación
desde (opcional): fecha desde
hasta (opcional): fecha hasta
```

**Response 200 OK**:
```json
{
  "success": true,
  "data": [
    {
      "id": 45,
      "orden_id": "ORD-00005",
      "evento": "Estado cambiado",
      "detalle": "Área Logística inició trabajo",
      "estado_global": "EN_PROCESO",
      "estado_parcial": "EN_PROGRESO",
      "area_id": "AREA-LOG",
      "timestamp": "2025-10-30T10:00:00Z",
      "actor": "Laura Fernández Torres"
    },
    {
      "id": 44,
      "orden_id": "ORD-00005",
      "evento": "Estado cambiado",
      "detalle": "Área Comercial completó trabajo",
      "estado_global": "EN_PROCESO",
      "estado_parcial": "COMPLETADA",
      "area_id": "AREA-COM",
      "timestamp": "2025-10-31T11:00:00Z",
      "actor": "Pedro Gómez Ruiz"
    }
  ],
  "pagination": {
    "total": 7,
    "limit": 50,
    "offset": 0
  }
}
```

---

### 3.2 Obtener Historial Global

**Endpoint**: `GET /api/v1/historial`

**Descripción**: Obtiene historial de todas las órdenes (auditoría global)

**Query Parameters**: Igual que historial de orden + `actor` filter

**Response**: Similar a historial de orden pero con múltiples orden_id

---

## 4. Áreas

### 4.1 Listar Áreas

**Endpoint**: `GET /api/v1/areas`

**Descripción**: Obtiene lista de áreas disponibles

**Query Parameters**:
```
activa (opcional): true|false
```

**Response 200 OK**:
```json
{
  "success": true,
  "data": [
    {
      "id": "AREA-TEC",
      "nombre": "Área Técnica",
      "responsable": "Ana López García",
      "contacto": "ana.lopez@conecta.com | +34-600-111-222",
      "descripcion": "Departamento técnico especializado...",
      "activa": true,
      "estadisticas": {
        "ordenes_activas": 5,
        "ordenes_completadas": 23
      }
    }
  ]
}
```

---

### 4.2 Obtener Detalle de Área

**Endpoint**: `GET /api/v1/areas/:id`

**Descripción**: Obtiene detalle de un área con sus asignaciones activas

**Response 200 OK**:
```json
{
  "success": true,
  "data": {
    "id": "AREA-TEC",
    "nombre": "Área Técnica",
    "responsable": "Ana López García",
    "contacto": "ana.lopez@conecta.com | +34-600-111-222",
    "activa": true,
    "asignaciones_activas": [
      {
        "orden_id": "ORD-00008",
        "titulo": "Actualización firmware cámaras",
        "estado_parcial": "EN_PROGRESO",
        "seg_acumulados": 1800
      }
    ],
    "estadisticas": {
      "total_ordenes": 28,
      "completadas": 23,
      "en_progreso": 3,
      "pendientes": 2,
      "tiempo_promedio_seg": 43200
    }
  }
}
```

---

## 5. KPIs y Dashboard

### 5.1 Obtener KPIs

**Endpoint**: `GET /api/v1/kpis`

**Descripción**: Obtiene métricas y KPIs del sistema

**Response 200 OK**:
```json
{
  "success": true,
  "data": {
    "ordenes": {
      "total": 8,
      "nuevas": 2,
      "en_proceso": 4,
      "completadas": 1,
      "cerradas_sin_solucion": 0,
      "parcialmente_vencidas": 0,
      "totalmente_vencidas": 1
    },
    "por_prioridad": {
      "critica": 3,
      "alta": 2,
      "media": 2,
      "baja": 1
    },
    "tiempos": {
      "tiempo_promedio_seg": 86400,
      "tiempo_total_seg": 1296000,
      "tiempo_promedio_horas": 24,
      "tiempo_total_horas": 360
    },
    "areas": {
      "mas_activa": "Área Técnica",
      "con_mas_ordenes": "Área Técnica",
      "tiempo_promedio_por_area": {
        "AREA-TEC": 72000,
        "AREA-COM": 54000,
        "AREA-SOP": 90000
      }
    }
  }
}
```

---

### 5.2 Dashboard

**Endpoint**: `GET /api/v1/dashboard`

**Descripción**: Obtiene datos agregados para dashboard

**Response 200 OK**:
```json
{
  "success": true,
  "data": {
    "resumen": {
      "ordenes_activas": 6,
      "ordenes_vencidas": 1,
      "areas_sobrecargadas": ["Área Técnica"],
      "sla_promedio_cumplimiento": 85.5
    },
    "ordenes_recientes": [...],
    "alertas": [
      {
        "tipo": "WARNING",
        "mensaje": "Orden ORD-00003 excedió SLA",
        "orden_id": "ORD-00003",
        "area_id": "AREA-SOP"
      }
    ]
  }
}
```

---

## 📝 Códigos de Error

| Código HTTP | Code | Descripción |
|-------------|------|-------------|
| 400 | VALIDATION_ERROR | Error de validación de entrada |
| 401 | UNAUTHORIZED | No autenticado (futuro) |
| 403 | FORBIDDEN | Sin permisos (futuro) |
| 404 | NOT_FOUND | Recurso no encontrado |
| 409 | CONFLICT | Conflicto de estado o duplicado |
| 422 | BUSINESS_RULE_VIOLATION | Violación de regla de negocio |
| 500 | INTERNAL_ERROR | Error interno del servidor |

---

## 🔐 Seguridad (Futuro)

### Autenticación
```
Authorization: Bearer <JWT_TOKEN>
```

### Rate Limiting
```
X-RateLimit-Limit: 100
X-RateLimit-Remaining: 95
X-RateLimit-Reset: 1635696000
```

---

## 📋 Headers Estándar

**Request**:
```
Content-Type: application/json
Accept: application/json
X-Request-ID: uuid-v4
```

**Response**:
```
Content-Type: application/json
X-Request-ID: uuid-v4
X-Response-Time: 45ms
```

---

**Última actualización**: 31 de Octubre, 2025  
**Versión API**: 1.0.0  
**Estado**: ✅ Documentación completa

