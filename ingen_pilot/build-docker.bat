@echo off
echo InGen Genes - Build via Docker (piloto DNA Extractor, 1.21.1)
echo Primeira vez pode levar 15-25 minutos.
docker build -t ingen-genes-builder .
if errorlevel 1 goto :error

for /f %%i in ('docker create ingen-genes-builder') do set CID=%%i
mkdir build-output\neoforge 2>nul
mkdir build-output\fabric 2>nul
docker cp %CID%:/mod/neoforge/build/libs/. build-output\neoforge\
docker cp %CID%:/mod/fabric/build/libs/. build-output\fabric\
docker rm %CID% >nul

echo.
echo JARs copiados para build-output\neoforge\ e build-output\fabric\
dir build-output\neoforge
dir build-output\fabric
pause
goto :eof

:error
echo Build falhou - veja o log acima.
pause
