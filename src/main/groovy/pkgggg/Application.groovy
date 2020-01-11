package pkgggg

import com.landoop.Evolution
import groovy.util.logging.Slf4j
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
import org.springframework.kafka.core.ConsumerFactory

import javax.annotation.PreDestroy

@Slf4j
@SpringBootApplication
class Application {

    static void main(String[] args) {
        SpringApplication.run(Application.class, args)
    }

}
