#!/bin/bash

# Espera o MockServer ficar disponível
while ! nc -z localhost 1080; do
  sleep 0.1 # aguarda por 1/10 do segundo antes de verificar novamente
done

# Configura as expectativas via API do MockServer
curl -X PUT "http://localhost:1080/mockserver/expectation" -d '{
    "httpRequest": {
          "method": "GET",
          "path": "/api/cadastro/cliente/conta/2fd0f2f6-58f3-4c8b-b510-ec20f6447b44"
        },
        "httpResponse": {
          "statusCode": 200,
          "body": "{\"id_conta\": \"2fd0f2f6-58f3-4c8b-b510-ec20f6447b44\", \"nome\": \"Ricky S. Turner\", \"conta_corrente_ativa\": true, \"limite_conta_corrente\": 1500, \"limite_diario_transferencia\": 1000}"
        }
}'

curl -X PUT "http://localhost:1080/mockserver/expectation" -d '{
    "httpRequest": {
          "method": "GET",
          "path": "/api/cadastro/cliente/conta/7859ee7a-edb5-416b-80c9-f6840007ddce"
        },
        "httpResponse": {
          "statusCode": 200,
          "body": "{\"id_conta\": \"7859ee7a-edb5-416b-80c9-f6840007ddce\", \"nome\": \"Erica N. Smith\", \"conta_corrente_ativa\": false, \"limite_conta_corrente\": 1000, \"limite_diario_transferencia\": 1000}"
        }
}'
