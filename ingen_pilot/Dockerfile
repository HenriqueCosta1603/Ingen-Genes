FROM eclipse-temurin:21-jdk-jammy

SHELL ["/bin/bash", "-c"]

WORKDIR /mod
COPY . .

RUN chmod +x ./gradlew

# Baixa NeoForge + Fabric + Architectury + Minecraft (~800MB-1GB na primeira vez).
# Acontece aqui dentro do container, com internet livre - sem as restricoes
# de rede que existem no ambiente do Claude.
RUN ./gradlew :neoforge:build --no-daemon 2>&1 | tee /tmp/build-neoforge.log; \
    if [ ${PIPESTATUS[0]} -ne 0 ]; then echo "--- FALHOU NEOFORGE ---"; cat /tmp/build-neoforge.log; exit 1; fi

RUN ./gradlew :fabric:build --no-daemon 2>&1 | tee /tmp/build-fabric.log; \
    if [ ${PIPESTATUS[0]} -ne 0 ]; then echo "--- FALHOU FABRIC (neoforge OK) ---"; cat /tmp/build-fabric.log; exit 1; fi

# JARs ficam em neoforge/build/libs/ e fabric/build/libs/
