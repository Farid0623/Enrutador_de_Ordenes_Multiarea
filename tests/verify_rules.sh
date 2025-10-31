#!/bin/bash
# verify_rules.sh - Script de verificación de reglas de estado
# E9: Tests de Verificación Manual

set -e  # Exit on error

echo "============================================"
echo "  E9: Verificación de Reglas de Estado"
echo "============================================"
echo ""

BASE_URL="http://localhost:8080/api/v1"

# Colores para output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Función para verificar si backend está corriendo
check_backend() {
    echo "Verificando backend..."
    if curl -s -f "$BASE_URL/ordenes" > /dev/null 2>&1 || [ "$?" -eq "22" ]; then
        echo -e "${GREEN}✓${NC} Backend disponible"
        return 0
    else
        echo -e "${RED}✗${NC} Backend no disponible en $BASE_URL"
        echo "Por favor, inicia el backend primero."
        exit 1
    fi
}

# Test 1: Dos áreas - una completada, otra en progreso
test_multiarea() {
    echo ""
    echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
    echo "  Test 1: Multiárea (COMPLETADA + EN_PROGRESO)"
    echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

    # Crear orden
    echo "1. Creando orden..."
    ORDEN_ID=$(curl -s -X POST "$BASE_URL/ordenes" \
      -H "Content-Type: application/json" \
      -d '{
        "titulo": "Test E9 - Multiárea",
        "descripcion": "Verificación de reglas de estado",
        "creador": "Script de Verificación",
        "prioridad": "MEDIA"
      }' | jq -r '.data.id')

    if [ -z "$ORDEN_ID" ] || [ "$ORDEN_ID" == "null" ]; then
        echo -e "${RED}✗${NC} Error creando orden"
        return 1
    fi
    echo -e "${GREEN}✓${NC} Orden creada: $ORDEN_ID"

    # Asignar área técnica
    echo "2. Asignando Área Técnica..."
    curl -s -X POST "$BASE_URL/ordenes/$ORDEN_ID/asignaciones" \
      -H "Content-Type: application/json" \
      -d '{"area_id": "AREA-TEC", "asignada_a": "Ana López"}' > /dev/null
    echo -e "${GREEN}✓${NC} Área Técnica asignada"

    # Asignar área comercial
    echo "3. Asignando Área Comercial..."
    curl -s -X POST "$BASE_URL/ordenes/$ORDEN_ID/asignaciones" \
      -H "Content-Type: application/json" \
      -d '{"area_id": "AREA-COM", "asignada_a": "Pedro Gómez"}' > /dev/null
    echo -e "${GREEN}✓${NC} Área Comercial asignada"

    # Completar área técnica
    echo "4. Completando Área Técnica..."
    curl -s -X PATCH "$BASE_URL/ordenes/$ORDEN_ID/areas/AREA-TEC" \
      -H "Content-Type: application/json" \
      -d '{"estado_parcial": "COMPLETADA"}' > /dev/null
    echo -e "${GREEN}✓${NC} Área Técnica completada"

    # Poner área comercial en progreso
    echo "5. Iniciando Área Comercial..."
    curl -s -X PATCH "$BASE_URL/ordenes/$ORDEN_ID/areas/AREA-COM" \
      -H "Content-Type: application/json" \
      -d '{"estado_parcial": "EN_PROGRESO"}' > /dev/null
    echo -e "${GREEN}✓${NC} Área Comercial en progreso"

    # Verificar estado global
    echo "6. Verificando estado global..."
    ESTADO=$(curl -s "$BASE_URL/ordenes/$ORDEN_ID" | jq -r '.data.estado_global')

    if [ "$ESTADO" == "EN_PROCESO" ]; then
        echo -e "${GREEN}✓✓✓ ÉXITO${NC}: Estado global correcto: $ESTADO"
        echo "    Regla verificada: Una EN_PROGRESO → Global EN_PROCESO"
        return 0
    else
        echo -e "${RED}✗✗✗ FALLO${NC}: Esperado EN_PROCESO, obtenido: $ESTADO"
        return 1
    fi
}

# Test 2: Todas completadas
test_todas_completadas() {
    echo ""
    echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
    echo "  Test 2: Todas las Áreas Completadas"
    echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

    # Crear orden
    echo "1. Creando orden..."
    ORDEN_ID=$(curl -s -X POST "$BASE_URL/ordenes" \
      -H "Content-Type: application/json" \
      -d '{
        "titulo": "Test E9 - Completadas",
        "descripcion": "Todas las áreas completadas",
        "creador": "Script de Verificación",
        "prioridad": "BAJA"
      }' | jq -r '.data.id')

    echo -e "${GREEN}✓${NC} Orden creada: $ORDEN_ID"

    # Asignar 2 áreas
    echo "2. Asignando áreas..."
    curl -s -X POST "$BASE_URL/ordenes/$ORDEN_ID/asignaciones" \
      -d '{"area_id": "AREA-TEC"}' > /dev/null
    curl -s -X POST "$BASE_URL/ordenes/$ORDEN_ID/asignaciones" \
      -d '{"area_id": "AREA-SOP"}' > /dev/null
    echo -e "${GREEN}✓${NC} 2 áreas asignadas"

    # Completar primera área
    echo "3. Completando Área Técnica..."
    curl -s -X PATCH "$BASE_URL/ordenes/$ORDEN_ID/areas/AREA-TEC" \
      -d '{"estado_parcial": "COMPLETADA"}' > /dev/null
    echo -e "${GREEN}✓${NC} Área Técnica completada"

    # Completar segunda área
    echo "4. Completando Área Soporte..."
    curl -s -X PATCH "$BASE_URL/ordenes/$ORDEN_ID/areas/AREA-SOP" \
      -d '{"estado_parcial": "COMPLETADA"}' > /dev/null
    echo -e "${GREEN}✓${NC} Área Soporte completada"

    # Verificar estado global
    echo "5. Verificando estado global..."
    ESTADO=$(curl -s "$BASE_URL/ordenes/$ORDEN_ID" | jq -r '.data.estado_global')

    if [ "$ESTADO" == "COMPLETADA" ]; then
        echo -e "${GREEN}✓✓✓ ÉXITO${NC}: Estado global correcto: $ESTADO"
        echo "    Regla verificada: Todas COMPLETADA → Global COMPLETADA"
        return 0
    else
        echo -e "${RED}✗✗✗ FALLO${NC}: Esperado COMPLETADA, obtenido: $ESTADO"
        return 1
    fi
}

# Test 3: Timeout (manual)
test_timeout_manual() {
    echo ""
    echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
    echo "  Test 3: Timeout SLA (Verificación Manual)"
    echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
    echo ""
    echo "Para verificar el timeout SLA:"
    echo ""
    echo "1. Crear orden:"
    echo "   curl -X POST $BASE_URL/ordenes -d '{"titulo": "Test Timeout"}'"
    echo ""
    echo "2. Asignar área:"
    echo "   curl -X POST $BASE_URL/ordenes/[ID]/asignaciones -d '{"area_id": "AREA-TEC"}'"
    echo ""
    echo "3. Iniciar trabajo:"
    echo "   curl -X PATCH $BASE_URL/ordenes/[ID]/areas/AREA-TEC -d '{"estado_parcial": "EN_PROGRESO"}'"
    echo ""
    echo "4. Esperar tick del temporizador (>= SLA_SEG segundos)"
    echo "   O forzar desde consola del navegador: temporizadorControls.tick()"
    echo ""
    echo "5. Verificar cambio a VENCIDA:"
    echo "   curl $BASE_URL/ordenes/[ID] | jq '.data.asignaciones[0].estado_parcial'"
    echo ""
    echo -e "${YELLOW}ℹ${NC}  Este test requiere tiempo de ejecución (>= ${SLA_SEG:-60}s)"
}

# Main execution
main() {
    # Verificar dependencias
    if ! command -v jq &> /dev/null; then
        echo -e "${RED}✗${NC} Error: jq no está instalado"
        echo "Instalar: brew install jq"
        exit 1
    fi

    # Verificar backend
    check_backend

    # Variables de configuración
    SLA_SEG=${SLA_SEG:-60}

    # Ejecutar tests
    PASSED=0
    FAILED=0

    if test_multiarea; then
        ((PASSED++))
    else
        ((FAILED++))
    fi

    if test_todas_completadas; then
        ((PASSED++))
    else
        ((FAILED++))
    fi

    test_timeout_manual

    # Resumen
    echo ""
    echo "============================================"
    echo "  Resumen de Verificación"
    echo "============================================"
    echo -e "Tests pasados:  ${GREEN}$PASSED${NC}"
    echo -e "Tests fallados: ${RED}$FAILED${NC}"
    echo ""

    if [ $FAILED -eq 0 ]; then
        echo -e "${GREEN}✓✓✓ TODOS LOS TESTS PASARON${NC}"
        echo ""
        exit 0
    else
        echo -e "${RED}✗✗✗ ALGUNOS TESTS FALLARON${NC}"
        echo ""
        exit 1
    fi
}

# Ejecutar
main

