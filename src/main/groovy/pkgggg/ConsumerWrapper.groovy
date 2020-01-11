package pkgggg

import com.landoop.Evolution
import groovy.util.logging.Slf4j
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerRecord
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.annotation.PartitionOffset
import org.springframework.kafka.annotation.TopicPartition
import org.springframework.stereotype.Component

@Slf4j
@Component
class ConsumerWrapper {

    @Autowired
    Producer<String, String> outputProducer

    @Value('${spring.kafka.output-topic}')
    String outputTopic

    @KafkaListener(
            topicPartitions = [
                    @TopicPartition(
                            topic = '${spring.kafka.input-topic}',
                            //forces reset to offset 0 every time like --from-beginning flag in cli
                            partitionOffsets = @PartitionOffset(partition = "0", initialOffset = "0")
                    )
            ], id = 'avroConsumer'
    )
    void listen(Evolution evolution) throws Exception {
        log.info('message found ' + evolution.toString())
        ProducerRecord record = new ProducerRecord(outputTopic, "${evolution.number1}".toString(), "${evolution.name} ${evolution.number2}".toString())
        outputProducer.send(record)
    }
}
