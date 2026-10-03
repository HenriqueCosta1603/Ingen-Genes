#!/bin/bash
set -e

JAVA_VER=$(java -version 2>&1 | grep -oE '"[0-9]+' | head -1 | tr -d '"')
if [ "$JAVA_VER" -lt "21" ] 2>/dev/null; then
  echo "AVISO: Java 21 necessario (detectado: Java $JAVA_VER)."
  echo "Baixe em: https://adoptium.net/ -> Temurin 21"
  exit 1
fi
echo "Java $JAVA_VER OK."

chmod +x ./gradlew

echo ""
echo "=== Etapa 1/2: compilando so o NeoForge (isola o problema, se houver) ==="
./gradlew :neoforge:build --no-daemon 2>&1 | tee build-neoforge.log
echo "NeoForge OK -> neoforge/build/libs/"
ls neoforge/build/libs/*.jar 2>/dev/null || true

echo ""
echo "=== Etapa 2/2: compilando o Fabric ==="
./gradlew :fabric:build --no-daemon 2>&1 | tee build-fabric.log
echo "Fabric OK -> fabric/build/libs/"
ls fabric/build/libs/*.jar 2>/dev/null || true
