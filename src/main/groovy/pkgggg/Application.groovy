package pkgggg

import com.landoop.Evolution
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerRecord
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.annotation.PartitionOffset
import org.springframework.kafka.annotation.TopicPartition
import org.springframework.kafka.listener.KafkaListenerErrorHandler

@SpringBootApplication
class Application implements CommandLineRunner {

    public static Logger logger = LoggerFactory.getLogger(Application.class);

    @Value('${spring.kafka.consumer.topic}')
    String topic

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args)
    }

    @KafkaListener(
            topicPartitions = [
                    @TopicPartition(
                            topic = '${spring.kafka.consumer.topic}',
                            partitionOffsets = @PartitionOffset(partition = "0", initialOffset = "0")
                    )
            ]
    )
    void listen(ConsumerRecord<?, ?> cr) throws Exception {
        logger.info('message found');
        logger.info(cr.toString());
    }

    @Override
    void run(String... args) throws Exception {
        logger.info('this ran...')
        Evolution evo = new Evolution('namey', 999, 9.99)
        ProducerRecord<String, Evolution> record = new ProducerRecord<String, Evolution>(topic, '999', evo)
        Evolution evo2 = new Evolution('namey', 1000, 10.00)
        ProducerRecord<String, Evolution> record2 = new ProducerRecord<String, Evolution>(topic, '1000', evo2)
        producer.send(record)
        producer.send(record2)
    }

    @Autowired
    Producer producer


}
