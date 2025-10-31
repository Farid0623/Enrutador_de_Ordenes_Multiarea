#!/bin/bash

# Script para compilar sin preview features (Java 17+)
# Actividad 1 - Conecta Seguros

echo "🏗️  Compilando Sistema de Gestión de Órdenes (Java 17+)..."

# Crear directorio de salida si no existe
mkdir -p out

# Compilar sin preview features
javac -d out -sourcepath src src/Main.java

if [ $? -eq 0 ]; then
    echo "✅ Compilación exitosa"
    echo ""
    echo "🚀 Ejecutando sistema..."
    echo ""

    # Ejecutar
    java -cp out Main
else
    echo "❌ Error en la compilación"
    echo ""
    echo "Posibles causas:"
    echo "- El código usa características de Java 21 (void main())"
    echo "- Cambia Main.java para usar: public static void main(String[] args)"
    exit 1
fi

