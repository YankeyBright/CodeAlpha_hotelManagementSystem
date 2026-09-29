@echo off
title Lumina Hotel Reservation System
echo ========================================================
echo   Lumina Hotel - Reservation Management System
echo   CodeAlpha Java Internship Task 4
echo ========================================================
echo.
echo Compiling project...
javac -d out src/com/hotel/model/*.java src/com/hotel/storage/*.java src/com/hotel/service/*.java src/com/hotel/ui/*.java src/com/hotel/Main.java
if %errorlevel% neq 0 (
    echo Compilation error occurred.
    pause
    exit /b %errorlevel%
)
echo Launching Application...
start javaw -cp out com.hotel.Main
exit