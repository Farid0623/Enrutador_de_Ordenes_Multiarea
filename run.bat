@echo off
REM Script para compilar y ejecutar en Windows
REM Sistema de Gestión de Órdenes - Actividad 1

echo Compilando Sistema de Gestion de Ordenes...

REM Crear directorio de salida
if not exist out mkdir out

REM Compilar con Java 21 (preview features)
javac --enable-preview --release 21 -d out -sourcepath src src\Main.java

if %errorlevel% == 0 (
    echo Compilacion exitosa
    echo.
    echo Ejecutando sistema...
    echo.

    REM Ejecutar
    java --enable-preview -cp out Main
) else (
    echo Error en la compilacion
    pause
    exit /b 1
)

