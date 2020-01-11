~/confluent-5.3.2/bin/kafka-avro-console-producer \
  --broker-list 127.0.0.1:9092 \
  --topic inputTopic \
  --property value.schema="$(cat src/main/avro/evolution-value.avsc)"

# Example:
# { "name":"someName", "number1": 123, "number2": 9.99 }