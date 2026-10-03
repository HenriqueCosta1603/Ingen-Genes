@echo off
java -version 2>&1 | findstr /C:"21." >nul
if errorlevel 1 (
    echo AVISO: Java 21 necessario. Baixe em https://adoptium.net/ -^> Temurin 21
    pause
    exit /b 1
)
echo Java 21 detectado.

echo.
echo === Etapa 1/2: compilando so o NeoForge ===
call gradlew.bat :neoforge:build
if errorlevel 1 goto :error
echo NeoForge OK -^> neoforge\build\libs\
dir neoforge\build\libs\*.jar

echo.
echo === Etapa 2/2: compilando o Fabric ===
call gradlew.bat :fabric:build
if errorlevel 1 goto :error
echo Fabric OK -^> fabric\build\libs\
dir fabric\build\libs\*.jar
pause
goto :eof

:error
echo Build falhou - veja o log acima.
pause
