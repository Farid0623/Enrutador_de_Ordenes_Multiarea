#!/bin/bash

# Script para compilar y ejecutar el Sistema de Gestión de Órdenes
# Actividad 1 - Conecta Seguros

echo "🏗️  Compilando Sistema de Gestión de Órdenes..."

# Crear directorio de salida si no existe
mkdir -p out

# Compilar con Java 21 (preview features)
javac --enable-preview --release 21 -d out -sourcepath src src/Main.java

if [ $? -eq 0 ]; then
    echo "✅ Compilación exitosa"
    echo ""
    echo "🚀 Ejecutando sistema..."
    echo ""

    # Ejecutar
    java --enable-preview -cp out Main
else
    echo "❌ Error en la compilación"
    exit 1
fi

