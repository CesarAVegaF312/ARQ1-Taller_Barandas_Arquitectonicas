#!/usr/bin/env bash
# Ronda 1 - "El atajo": copia sobre el proyecto la implementacion de la alerta
# de stock bajo tal como la dejaria un agente apurado.
#
# Uso (desde la carpeta del taller):
#   ./scripts/aplicar-atajo.sh                      # aplica sobre inventarium-pedidos/
#   ./scripts/aplicar-atajo.sh ruta/a/mi-proyecto   # aplica sobre otra copia
set -euo pipefail

AQUI="$(cd "$(dirname "$0")" && pwd)"
RAIZ="$(dirname "$AQUI")"
DESTINO="${1:-$RAIZ/inventarium-pedidos}"

if [ ! -f "$DESTINO/pom.xml" ]; then
  echo "No encuentro $DESTINO/pom.xml. Indique la carpeta del proyecto." >&2
  exit 1
fi

cp -r "$RAIZ/atajo/src/." "$DESTINO/src/"

echo "Atajo aplicado en $DESTINO. Archivos tocados:"
(cd "$RAIZ/atajo" && find src -type f | sort | sed 's/^/  /')
echo
echo "Siguiente paso: cd \"$DESTINO\" && mvn -B clean verify"
