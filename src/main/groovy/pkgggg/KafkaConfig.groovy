package pkgggg

import com.landoop.Evolution
import io.confluent.kafka.schemaregistry.client.CachedSchemaRegistryClient
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient
import io.confluent.kafka.serializers.KafkaAvroDeserializer
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig
import io.confluent.kafka.serializers.KafkaAvroSerializer
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.IntegerSerializer
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.config.KafkaStreamsConfiguration
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory

@EnableKafka
@Configuration
class KafkaConfig {



    @Value('${spring.kafka.consumer.group-id}')
    String groupId
    @Value('${spring.kafka.consumer.auto-offset-reset}')
    String autoOffsetReset
    @Value('${spring.kafka.consumer.schema-registry-url}')
    String registry

    @Value('${spring.kafka.consumer.bootstrap-servers}')
    String bootstraps
    @Value('${spring.kafka.consumer.key-deserializer}')
    String keyDeserializer
    @Value('${spring.kafka.consumer.value-deserializer}')
    String valueDeserializer
    @Value('${spring.kafka.producer.key-serializer}')
    String keySerializer
    @Value('${spring.kafka.producer.value-serializer}')
    String valueSerializer

    @Bean
    ConsumerFactory<String, Object> consumerFactory() {
        Map<String, Object> consProps = new HashMap<>()
        consProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstraps)
        consProps.put(ConsumerConfig.GROUP_ID_CONFIG, groupId)
        consProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset)
        consProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keyDeserializer)
        consProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueDeserializer)
        AvroDeserializer evolutionAvroDeserializer = new AvroDeserializer(registry, consProps)
        return new DefaultKafkaConsumerFactory<>(
                consProps,
                new StringDeserializer(),
                evolutionAvroDeserializer
        )
    }

    @Bean
    Producer<String, String> outputProducer() {
        Map<String, Object> prodProps = new HashMap<>()
        prodProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstraps)
        prodProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer)
        prodProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer)
//        prodProps.put(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, registry)
//        SchemaRegistryClient schemaRegistryClient =
//                new CachedSchemaRegistryClient(registry, 1000)
        return new KafkaProducer<String, String>(
                prodProps,
                new StringSerializer(),
                new StringSerializer()
        )
    }

    @Bean
    Producer<String, Object> avroProducer() {
        Map<String, Object> prodProps = new HashMap<>()
        prodProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstraps)
        prodProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer)
        prodProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer)
        prodProps.put(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, registry)
        SchemaRegistryClient schemaRegistryClient =
                new CachedSchemaRegistryClient(registry, 1000)
        return new KafkaProducer<String, Object>(
                prodProps,
                new StringSerializer(),
                new KafkaAvroSerializer(schemaRegistryClient, prodProps)
        )
    }

    @Bean
    Consumer<String, String> outputConsumer() {
        Properties consProps = new Properties()
        consProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstraps)
        consProps.put(ConsumerConfig.GROUP_ID_CONFIG, groupId)
        consProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset)
        consProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, 'org.apache.kafka.common.serialization.StringDeserializer')
        consProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, 'org.apache.kafka.common.serialization.StringDeserializer')
        return new KafkaConsumer(consProps, new StringDeserializer(),new StringDeserializer())
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory(ContainerErrorHandler containerErrorHandler) {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory())
        factory.setErrorHandler(containerErrorHandler)
        return factory
    }


}
