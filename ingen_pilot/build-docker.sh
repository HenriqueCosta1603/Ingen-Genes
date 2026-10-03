#!/bin/bash
set -e
echo "InGen Genes - Build via Docker (piloto DNA Extractor, 1.21.1)"
echo "Primeira vez pode levar 15-25 minutos (baixa NeoForge + Fabric + Architectury + MC)."
docker build -t ingen-genes-builder .

CID=$(docker create ingen-genes-builder)
mkdir -p build-output/neoforge build-output/fabric
docker cp "$CID":/mod/neoforge/build/libs/. build-output/neoforge/ 2>/dev/null || true
docker cp "$CID":/mod/fabric/build/libs/. build-output/fabric/ 2>/dev/null || true
docker rm "$CID" > /dev/null

echo ""
echo "JARs copiados para:"
echo "  build-output/neoforge/  (versao NeoForge)"
echo "  build-output/fabric/    (versao Fabric)"
ls -la build-output/neoforge/ build-output/fabric/ 2>/dev/null || true
