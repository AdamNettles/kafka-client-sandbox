package pkgggg

import com.landoop.Evolution
import io.confluent.kafka.schemaregistry.client.CachedSchemaRegistryClient
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient
import io.confluent.kafka.serializers.KafkaAvroDeserializer
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig
import org.apache.kafka.common.header.Headers
import org.apache.kafka.common.serialization.Deserializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.listener.ConsumerProperties
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer2

class AvroDeserializer implements Deserializer<Evolution> {

    ErrorHandlingDeserializer2 inner
    SchemaRegistryClient schemaRegistryClient

    AvroDeserializer(String schemaRegistryUrl, Map<String, Object> consProps) {
        this.schemaRegistryClient = new CachedSchemaRegistryClient(schemaRegistryUrl, 1000)
        consProps.put(KafkaAvroDeserializerConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl)
        consProps.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, "true")
        inner = new ErrorHandlingDeserializer2 ( new KafkaAvroDeserializer(schemaRegistryClient, consProps) )
    }

    @Override
    void configure(Map<String, ?> configs, boolean isKey) {
        inner.configure(configs, isKey)
    }

    @Override
    Evolution deserialize(String topic, byte[] data) {
        schemaRegistryClient.getSchemaMetadata()
        return inner.deserialize(topic, data) as Evolution
    }

    @Override
    Evolution deserialize(String topic, Headers headers, byte[] data) {
        return inner.deserialize(topic, headers, data) as Evolution
    }

    @Override
    void close() {
        inner.close()
    }
}
