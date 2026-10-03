@echo off
setlocal enabledelayedexpansion
cd /d "%~dp0"

echo ============================================
echo  InGen Genes - Dev Build (Windows)
echo ============================================
echo.

REM --- Detecta se da pra usar Docker; senao cai pro caminho nativo (Java 21) ---
set "USE_DOCKER=0"
docker info >nul 2>&1
if not errorlevel 1 set "USE_DOCKER=1"

if "%USE_DOCKER%"=="1" (
    echo [OK] Docker detectado e rodando - build via Docker.
    echo.
    echo Compilando... ^(primeira vez pode levar 15-25 min^)
    call build-docker.bat
    if errorlevel 1 (
        echo [ERRO] Build via Docker falhou. Veja a saida acima.
        pause
        exit /b 1
    )
    set "JARDIR=build-output\neoforge"
) else (
    echo [INFO] Docker nao disponivel - build nativo ^(Java 21^).
    echo.
    java -version 2>&1 | findstr /C:"21." >nul
    if errorlevel 1 (
        echo [ERRO] Precisa do Java 21 instalado.
        echo Baixe em: https://adoptium.net/temurin/releases/?version=21
        echo Depois de instalar, feche esta janela e rode dev.bat de novo.
        pause
        exit /b 1
    )
    echo [OK] Java 21 detectado.
    echo.
    echo Compilando... ^(primeira vez pode levar 15-25 min, baixando MC+NeoForge+Fabric+Architectury^)
    call build-native.bat
    if errorlevel 1 (
        echo [ERRO] Build nativo falhou. Veja a saida acima ^(ou build-neoforge.log^).
        pause
        exit /b 1
    )
    set "JARDIR=neoforge\build\libs"
)
echo.

REM --- Acha o .jar final (pula -dev.jar, -dev-shadow.jar e -sources.jar) ---
set "JARFILE="
for %%f in ("%JARDIR%\*.jar") do (
    set "FNAME=%%~nxf"
    echo !FNAME! | findstr /i /c:"sources" /c:"-dev" >nul
    if errorlevel 1 set "JARFILE=%%f"
)
if "%JARFILE%"=="" (
    echo [ERRO] Build terminou mas nao achei nenhum .jar principal em %JARDIR%
    dir "%JARDIR%\*.jar"
    pause
    exit /b 1
)
echo Jar gerado: %JARFILE%
echo.

REM --- Garante que a pasta mods existe ---
set "MODSDIR=%appdata%\.minecraft\mods"
if not exist "%MODSDIR%" (
    echo Criando pasta de mods: %MODSDIR%
    mkdir "%MODSDIR%"
)

REM --- Remove jar antigo do InGen Genes (neoforge) pra nao acumular versao velha ---
if exist "%MODSDIR%\ingen_genes-neoforge-*.jar" del /q "%MODSDIR%\ingen_genes-neoforge-*.jar"

REM --- Copia o jar novo pra pasta mods ---
copy /y "%JARFILE%" "%MODSDIR%\" >nul
if errorlevel 1 (
    echo [ERRO] Nao consegui copiar o jar para %MODSDIR%
    pause
    exit /b 1
)

echo.
echo ============================================
echo  PRONTO!
echo  Jar instalado em: %MODSDIR%
echo ============================================
echo Abra o launcher do Minecraft, escolha a versao "neoforge-21.1.172"
echo e entre num mundo pra testar o DNA Extractor.
echo.
pause
