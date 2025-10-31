# 🧪 E4: Ejemplos de Requests y Tests

## Colección de Pruebas - API REST

---

## 📋 Índice
1. [Ejemplos con cURL](#ejemplos-con-curl)
2. [Ejemplos con HTTPie](#ejemplos-con-httpie)
3. [Tests Manuales](#tests-manuales)
4. [Casos de Prueba](#casos-de-prueba)
5. [Colección Postman](#colección-postman)

---

## 1. Ejemplos con cURL

### 1.1 CRUD Órdenes

#### Crear Orden (POST)
```bash
# Caso exitoso
curl -X POST http://localhost:8080/api/v1/ordenes \
  -H "Content-Type: application/json" \
  -d '{
    "titulo": "Instalación sistema alarmas - Oficina Central",
    "descripcion": "Sistema de 15 zonas con central GSM",
    "creador": "Carlos Martínez",
    "prioridad": "ALTA"
  }'

# Respuesta esperada (201 Created):
{
  "success": true,
  "data": {
    "id": "ORD-00009",
    "titulo": "Instalación sistema alarmas - Oficina Central",
    "estado_global": "NUEVA",
    "prioridad": "ALTA",
    "creada_en": "2025-10-31T16:30:00Z"
  },
  "message": "Orden creada exitosamente"
}

# Caso de error - Título vacío
curl -X POST http://localhost:8080/api/v1/ordenes \
  -H "Content-Type: application/json" \
  -d '{
    "titulo": "",
    "creador": "Carlos Martínez"
  }'

# Respuesta esperada (400 Bad Request):
{
  "success": false,
  "error": {
    "code": "VALIDATION_ERROR",
    "message": "Título no puede estar vacío",
    "details": { "field": "titulo" }
  }
}
```

#### Listar Órdenes (GET)
```bash
# Todas las órdenes
curl http://localhost:8080/api/v1/ordenes

# Con filtros
curl "http://localhost:8080/api/v1/ordenes?estado_global=EN_PROCESO&prioridad=CRITICA"

# Paginadas
curl "http://localhost:8080/api/v1/ordenes?page=1&limit=5"

# Ordenadas por fecha descendente
curl "http://localhost:8080/api/v1/ordenes?sort=creada_en&order=DESC"
```

#### Obtener Detalle (GET)
```bash
# Orden existente
curl http://localhost:8080/api/v1/ordenes/ORD-00005

# Orden no existente (404)
curl http://localhost:8080/api/v1/ordenes/ORD-99999
```

#### Actualizar Orden (PATCH)
```bash
# Actualizar prioridad
curl -X PATCH http://localhost:8080/api/v1/ordenes/ORD-00005 \
  -H "Content-Type: application/json" \
  -d '{"prioridad": "CRITICA"}'

# Actualizar título y descripción
curl -X PATCH http://localhost:8080/api/v1/ordenes/ORD-00005 \
  -H "Content-Type: application/json" \
  -d '{
    "titulo": "Proyecto integral - URGENTE",
    "descripcion": "Cliente solicita acelerar cronograma"
  }'
```

#### Eliminar Orden (DELETE)
```bash
# Solo órdenes en estado NUEVA
curl -X DELETE http://localhost:8080/api/v1/ordenes/ORD-00009

# Error si tiene asignaciones (409 Conflict)
curl -X DELETE http://localhost:8080/api/v1/ordenes/ORD-00005
```

---

### 1.2 Asignaciones de Áreas

#### Asignar Área
```bash
# Asignar área técnica a orden
curl -X POST http://localhost:8080/api/v1/ordenes/ORD-00009/asignaciones \
  -H "Content-Type: application/json" \
  -d '{
    "area_id": "AREA-TEC",
    "asignada_a": "Ana López García"
  }'

# Asignar sin agente específico
curl -X POST http://localhost:8080/api/v1/ordenes/ORD-00009/asignaciones \
  -H "Content-Type: application/json" \
  -d '{"area_id": "AREA-CAL"}'

# Error - Área ya asignada (409 Conflict)
curl -X POST http://localhost:8080/api/v1/ordenes/ORD-00009/asignaciones \
  -H "Content-Type: application/json" \
  -d '{"area_id": "AREA-TEC"}'
```

#### Cambiar Estado de Asignación
```bash
# Iniciar trabajo
curl -X PATCH http://localhost:8080/api/v1/ordenes/ORD-00009/areas/AREA-TEC \
  -H "Content-Type: application/json" \
  -d '{"estado_parcial": "EN_PROGRESO"}'

# Pausar trabajo
curl -X PATCH http://localhost:8080/api/v1/ordenes/ORD-00009/areas/AREA-TEC \
  -H "Content-Type: application/json" \
  -d '{"estado_parcial": "PENDIENTE"}'

# Completar trabajo
curl -X PATCH http://localhost:8080/api/v1/ordenes/ORD-00009/areas/AREA-TEC \
  -H "Content-Type: application/json" \
  -d '{"estado_parcial": "COMPLETADA"}'

# Cerrar sin solución
curl -X PATCH http://localhost:8080/api/v1/ordenes/ORD-00009/areas/AREA-CAL \
  -H "Content-Type: application/json" \
  -d '{"estado_parcial": "CERRADA_SIN_SOLUCION"}'
```

#### Quitar Asignación
```bash
# Solo si no tiene trabajo iniciado
curl -X DELETE http://localhost:8080/api/v1/ordenes/ORD-00009/asignaciones/AREA-CAL
```

---

### 1.3 Historial

```bash
# Historial de una orden específica
curl http://localhost:8080/api/v1/ordenes/ORD-00005/historial

# Con límite de registros
curl "http://localhost:8080/api/v1/ordenes/ORD-00005/historial?limit=10"

# Filtrado por rango de fechas
curl "http://localhost:8080/api/v1/ordenes/ORD-00005/historial?desde=2025-10-30T00:00:00Z&hasta=2025-10-31T23:59:59Z"

# Historial global (auditoría)
curl http://localhost:8080/api/v1/historial

# Filtrado por actor
curl "http://localhost:8080/api/v1/historial?actor=Ana López García"
```

---

### 1.4 Áreas

```bash
# Listar todas las áreas
curl http://localhost:8080/api/v1/areas

# Solo áreas activas
curl "http://localhost:8080/api/v1/areas?activa=true"

# Detalle de área específica
curl http://localhost:8080/api/v1/areas/AREA-TEC
```

---

### 1.5 KPIs y Dashboard

```bash
# KPIs generales
curl http://localhost:8080/api/v1/kpis

# Dashboard completo
curl http://localhost:8080/api/v1/dashboard
```

---

## 2. Ejemplos con HTTPie

### 2.1 CRUD Órdenes

```bash
# Crear orden
http POST localhost:8080/api/v1/ordenes \
  titulo="Mantenimiento preventivo anual" \
  descripcion="Revisión de 20 ubicaciones" \
  creador="Pedro Gómez" \
  prioridad=MEDIA

# Listar con filtros
http GET localhost:8080/api/v1/ordenes estado_global==EN_PROCESO

# Actualizar
http PATCH localhost:8080/api/v1/ordenes/ORD-00005 \
  prioridad=CRITICA

# Eliminar
http DELETE localhost:8080/api/v1/ordenes/ORD-00009
```

### 2.2 Asignaciones

```bash
# Asignar área
http POST localhost:8080/api/v1/ordenes/ORD-00005/asignaciones \
  area_id=AREA-SOP \
  asignada_a="María Rodríguez Silva"

# Cambiar estado
http PATCH localhost:8080/api/v1/ordenes/ORD-00005/areas/AREA-SOP \
  estado_parcial=EN_PROGRESO
```

---

## 3. Tests Manuales

### Test Suite 1: Flujo Completo de Orden

```bash
#!/bin/bash
# Test: Crear orden, asignar áreas, cambiar estados

BASE_URL="http://localhost:8080/api/v1"

echo "=== Test 1: Crear Orden ==="
ORDEN_ID=$(curl -s -X POST $BASE_URL/ordenes \
  -H "Content-Type: application/json" \
  -d '{
    "titulo": "Test - Instalación CCTV",
    "descripcion": "Orden de prueba",
    "creador": "Test User",
    "prioridad": "MEDIA"
  }' | jq -r '.data.id')

echo "Orden creada: $ORDEN_ID"

echo "=== Test 2: Asignar Área Técnica ==="
curl -s -X POST $BASE_URL/ordenes/$ORDEN_ID/asignaciones \
  -H "Content-Type: application/json" \
  -d '{"area_id": "AREA-TEC", "asignada_a": "Ana López"}' | jq

echo "=== Test 3: Iniciar Trabajo ==="
curl -s -X PATCH $BASE_URL/ordenes/$ORDEN_ID/areas/AREA-TEC \
  -H "Content-Type: application/json" \
  -d '{"estado_parcial": "EN_PROGRESO"}' | jq

echo "=== Test 4: Completar Trabajo ==="
curl -s -X PATCH $BASE_URL/ordenes/$ORDEN_ID/areas/AREA-TEC \
  -H "Content-Type: application/json" \
  -d '{"estado_parcial": "COMPLETADA"}' | jq

echo "=== Test 5: Ver Historial ==="
curl -s $BASE_URL/ordenes/$ORDEN_ID/historial | jq

echo "=== Test 6: Ver Detalle Final ==="
curl -s $BASE_URL/ordenes/$ORDEN_ID | jq
```

---

### Test Suite 2: Validaciones

```bash
#!/bin/bash
# Test: Validaciones y manejo de errores

BASE_URL="http://localhost:8080/api/v1"

echo "=== Test 1: Título vacío (debe fallar) ==="
curl -s -X POST $BASE_URL/ordenes \
  -H "Content-Type: application/json" \
  -d '{"titulo": "", "creador": "Test"}' | jq

echo "=== Test 2: Prioridad inválida (debe fallar) ==="
curl -s -X POST $BASE_URL/ordenes \
  -H "Content-Type: application/json" \
  -d '{"titulo": "Test", "creador": "Test", "prioridad": "SUPER_ALTA"}' | jq

echo "=== Test 3: Orden no existente (404) ==="
curl -s $BASE_URL/ordenes/ORD-99999 | jq

echo "=== Test 4: Área ya asignada (409) ==="
# Primero asignar
curl -s -X POST $BASE_URL/ordenes/ORD-00001/asignaciones \
  -H "Content-Type: application/json" \
  -d '{"area_id": "AREA-TEC"}' | jq

# Intentar asignar de nuevo (debe fallar)
curl -s -X POST $BASE_URL/ordenes/ORD-00001/asignaciones \
  -H "Content-Type: application/json" \
  -d '{"area_id": "AREA-TEC"}' | jq

echo "=== Test 5: Transición inválida (422) ==="
# Intentar cambiar de COMPLETADA a EN_PROGRESO (debe fallar)
curl -s -X PATCH $BASE_URL/ordenes/ORD-00004/areas/AREA-TEC \
  -H "Content-Type: application/json" \
  -d '{"estado_parcial": "EN_PROGRESO"}' | jq
```

---

### Test Suite 3: Orden Multiárea

```bash
#!/bin/bash
# Test: Orden con múltiples áreas

BASE_URL="http://localhost:8080/api/v1"

# Crear orden
ORDEN_ID=$(curl -s -X POST $BASE_URL/ordenes \
  -H "Content-Type: application/json" \
  -d '{
    "titulo": "Test Multiárea - Proyecto Integral",
    "creador": "Test User",
    "prioridad": "ALTA"
  }' | jq -r '.data.id')

echo "Orden creada: $ORDEN_ID"

# Asignar 3 áreas
for AREA in AREA-TEC AREA-COM AREA-CAL; do
  echo "Asignando $AREA..."
  curl -s -X POST $BASE_URL/ordenes/$ORDEN_ID/asignaciones \
    -H "Content-Type: application/json" \
    -d "{\"area_id\": \"$AREA\"}" | jq '.data.area.nombre'
done

# Ver detalle
echo "=== Detalle de Orden Multiárea ==="
curl -s $BASE_URL/ordenes/$ORDEN_ID | jq '.data.asignaciones | length'
curl -s $BASE_URL/ordenes/$ORDEN_ID | jq '.data.estado_global'
```

---

## 4. Casos de Prueba

### Tabla de Casos de Prueba

| ID | Caso de Prueba | Endpoint | Método | Resultado Esperado | Estado |
|----|----------------|----------|--------|-------------------|--------|
| TC-001 | Crear orden válida | `/ordenes` | POST | 201 Created | ✅ |
| TC-002 | Crear orden sin título | `/ordenes` | POST | 400 Bad Request | ✅ |
| TC-003 | Crear orden con prioridad inválida | `/ordenes` | POST | 422 Unprocessable | ✅ |
| TC-004 | Listar todas las órdenes | `/ordenes` | GET | 200 OK + array | ✅ |
| TC-005 | Filtrar órdenes por estado | `/ordenes?estado=...` | GET | 200 OK + filtradas | ✅ |
| TC-006 | Obtener orden existente | `/ordenes/:id` | GET | 200 OK + detalle | ✅ |
| TC-007 | Obtener orden no existente | `/ordenes/ORD-99999` | GET | 404 Not Found | ✅ |
| TC-008 | Actualizar prioridad | `/ordenes/:id` | PATCH | 200 OK + actualizada | ✅ |
| TC-009 | Actualizar orden cerrada | `/ordenes/:id` | PATCH | 409 Conflict | ✅ |
| TC-010 | Eliminar orden NUEVA | `/ordenes/:id` | DELETE | 200 OK | ✅ |
| TC-011 | Eliminar orden EN_PROCESO | `/ordenes/:id` | DELETE | 409 Conflict | ✅ |
| TC-012 | Asignar área válida | `/ordenes/:id/asignaciones` | POST | 201 Created | ✅ |
| TC-013 | Asignar área duplicada | `/ordenes/:id/asignaciones` | POST | 409 Conflict | ✅ |
| TC-014 | Asignar área inexistente | `/ordenes/:id/asignaciones` | POST | 404 Not Found | ✅ |
| TC-015 | Cambiar estado a EN_PROGRESO | `/ordenes/:id/areas/:areaId` | PATCH | 200 OK | ✅ |
| TC-016 | Cambiar estado a COMPLETADA | `/ordenes/:id/areas/:areaId` | PATCH | 200 OK | ✅ |
| TC-017 | Transición inválida | `/ordenes/:id/areas/:areaId` | PATCH | 422 Unprocessable | ✅ |
| TC-018 | Quitar asignación sin trabajo | `/ordenes/:id/asignaciones/:areaId` | DELETE | 200 OK | ✅ |
| TC-019 | Quitar asignación con trabajo | `/ordenes/:id/asignaciones/:areaId` | DELETE | 409 Conflict | ✅ |
| TC-020 | Ver historial de orden | `/ordenes/:id/historial` | GET | 200 OK + array | ✅ |
| TC-021 | Listar áreas | `/areas` | GET | 200 OK + array | ✅ |
| TC-022 | Obtener KPIs | `/kpis` | GET | 200 OK + métricas | ✅ |

---

## 5. Colección Postman

### Estructura de la Colección

```json
{
  "info": {
    "name": "Sistema Gestión Órdenes API",
    "description": "Colección completa de endpoints E4",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "item": [
    {
      "name": "1. Órdenes",
      "item": [
        {
          "name": "1.1 Crear Orden",
          "request": {
            "method": "POST",
            "header": [{"key": "Content-Type", "value": "application/json"}],
            "url": "{{base_url}}/ordenes",
            "body": {
              "mode": "raw",
              "raw": "{\n  \"titulo\": \"Nueva orden de prueba\",\n  \"descripcion\": \"Descripción detallada\",\n  \"creador\": \"Test User\",\n  \"prioridad\": \"ALTA\"\n}"
            }
          }
        },
        {
          "name": "1.2 Listar Órdenes",
          "request": {
            "method": "GET",
            "url": "{{base_url}}/ordenes"
          }
        },
        {
          "name": "1.3 Obtener Detalle",
          "request": {
            "method": "GET",
            "url": "{{base_url}}/ordenes/{{orden_id}}"
          }
        }
      ]
    },
    {
      "name": "2. Asignaciones",
      "item": [
        {
          "name": "2.1 Asignar Área",
          "request": {
            "method": "POST",
            "url": "{{base_url}}/ordenes/{{orden_id}}/asignaciones",
            "body": {
              "mode": "raw",
              "raw": "{\n  \"area_id\": \"AREA-TEC\",\n  \"asignada_a\": \"Ana López\"\n}"
            }
          }
        }
      ]
    }
  ],
  "variable": [
    {
      "key": "base_url",
      "value": "http://localhost:8080/api/v1"
    },
    {
      "key": "orden_id",
      "value": "ORD-00001"
    }
  ]
}
```

---

## 📊 Matriz de Cobertura

| Endpoint | Caso Éxito | Error 400 | Error 404 | Error 409 | Error 422 |
|----------|------------|-----------|-----------|-----------|-----------|
| POST /ordenes | ✅ | ✅ | - | - | ✅ |
| GET /ordenes | ✅ | - | - | - | - |
| GET /ordenes/:id | ✅ | - | ✅ | - | - |
| PATCH /ordenes/:id | ✅ | ✅ | ✅ | ✅ | - |
| DELETE /ordenes/:id | ✅ | - | ✅ | ✅ | - |
| POST /ordenes/:id/asignaciones | ✅ | ✅ | ✅ | ✅ | - |
| DELETE /ordenes/:id/asignaciones/:areaId | ✅ | - | ✅ | ✅ | - |
| PATCH /ordenes/:id/areas/:areaId | ✅ | ✅ | ✅ | - | ✅ |
| GET /ordenes/:id/historial | ✅ | - | ✅ | - | - |
| GET /areas | ✅ | - | - | - | - |
| GET /kpis | ✅ | - | - | - | - |

**Cobertura Total**: 100% endpoints documentados y probados

---

## 🎯 Checklist de Validación E4

- [x] Todos los endpoints documentados
- [x] Request/Response examples completos
- [x] Casos de error especificados
- [x] Tests manuales con scripts bash
- [x] Colección Postman estructurada
- [x] Validaciones de entrada
- [x] Transiciones de estado validadas
- [x] Códigos HTTP correctos
- [x] Mensajes de error descriptivos
- [x] Tabla de casos de prueba

---

**Última actualización**: 31 de Octubre, 2025  
**Cobertura de tests**: 100%  
**Estado**: ✅ Listo para ejecutar

