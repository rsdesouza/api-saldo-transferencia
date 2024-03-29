#!/bin/bash

export AWS_ACCESS_KEY_ID=test
export AWS_SECRET_ACCESS_KEY=test
export AWS_DEFAULT_REGION=us-east-1
export AWS_ENDPOINT_URL=http://localhost:4566

aws --endpoint-url=http://localhost:4566 sqs create-queue --queue-name reprocessamento-bacen-queue

aws --endpoint-url=http://localhost:4566 lambda create-function --function-name notificar-bacen-lambda  \
  --zip-file fileb://lambda_function.zip \
  --handler lambda_function.lambda_handler \
  --runtime python3.8 \
  --role arn:aws:iam::123456789012:role/execution_role

queue_arn=$(aws --endpoint-url=http://localhost:4566 sqs get-queue-attributes --queue-url http://localstack:4566/000000000000/reprocessamento-bacen-queue --attribute-names QueueArn --query Attributes.QueueArn --output text)

aws --endpoint-url=http://localhost:4566 lambda create-event-source-mapping \
  --function-name notificar-bacen-lambda \
  --batch-size 10 \
  --event-source-arn $queue_arn
