#!/bin/bash

# Aguarde o LocalStack estar totalmente operacional
echo "Aguardando o LocalStack..."
while ! nc -z localhost 4566; do
  sleep 1
done
echo "LocalStack pronto."

# Defina as variáveis de ambiente para o LocalStack
export AWS_ACCESS_KEY_ID=test
export AWS_SECRET_ACCESS_KEY=test
export AWS_DEFAULT_REGION=us-east-1
export AWS_ENDPOINT_URL=http://localhost:4566

# Crie a fila SQS
aws --endpoint-url=http://localhost:4566 sqs create-queue --queue-name reprocessamento-bacen-queue

# Crie a função Lambda
aws --endpoint-url=http://localhost:4566 lambda create-function --function-name notificar-bacen-lambda  \
  --zip-file fileb://lambda_function.zip \
  --handler lambda_function.lambda_handler \
  --runtime python3.8 \
  --role arn:aws:iam::123456789012:role/execution_role

# Configure a função Lambda para ser acionada pela fila SQS
queue_arn=$(aws --endpoint-url=http://localhost:4566 sqs get-queue-attributes --queue-url http://localhost:4566/000000000000/reprocessamento-bacen-queue --attribute-names QueueArn --query Attributes.QueueArn --output text)

aws --endpoint-url=http://localhost:4566 lambda create-event-source-mapping \
  --function-name notificar-bacen-lambda \
  --batch-size 10 \
  --event-source-arn $queue_arn
