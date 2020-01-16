import com.landoop.Evolution
import groovy.util.logging.Slf4j
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerRecord
import org.junit.ClassRule
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.config.KafkaListenerEndpointRegistry
import org.springframework.kafka.test.rule.EmbeddedKafkaRule
import org.springframework.kafka.test.utils.KafkaTestUtils
import org.springframework.test.context.ActiveProfiles
import pkgggg.Application
import pkgggg.ConsumerWrapper
import spock.lang.Shared
import spock.lang.Specification

import java.util.regex.Pattern

@Slf4j
@SpringBootTest(classes = [Application, ConsumerWrapper])
@ActiveProfiles('local-test')
class TestSpec extends Specification {

    @Value('${spring.kafka.test-field}')
    String testField

    static final String INPUT_TOPIC = 'inputTopic'
    static final String OUTPUT_TOPIC = 'outputTopic'

    @Autowired
    KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry

    @Autowired
    Producer<String, Object> avroProducer

    @Autowired
    Producer outputProducer

    @Autowired
    Consumer<String, String> outputConsumer

    @Shared
    @ClassRule
    EmbeddedKafkaRule embeddedKafkaRule = new EmbeddedKafkaRule(1, true, 1, INPUT_TOPIC, OUTPUT_TOPIC, INPUT_TOPIC+'-dummy')

    /**
     * For now, this Test requires the SR to be up and running from the docker-compose.yml
     * @return
     */
    def 'test it'() {
        given:
        Evolution evo = new Evolution('namey', 999, 9.99)
        ProducerRecord<String, Evolution> record = new ProducerRecord<String, Evolution>(INPUT_TOPIC+'-dummy', '999', evo)
        URL url = new URL('http://localhost:8001/#/cluster/default/schema/test-topic-value/version/latest')
        HttpURLConnection con = (HttpURLConnection) url.openConnection()
        con.setRequestMethod("GET")
        assert testField != null
        assert testField == 'value'
        assert avroProducer != null
        assert outputProducer != null
        assert outputConsumer != null
        assert con.getResponseCode() == 200

        when:
        outputConsumer.subscribe(Pattern.compile(OUTPUT_TOPIC))
        avroProducer.send(record)
//        ConsumerRecords<String, String> consumerRecords = outputConsumer.poll(Duration.of(30, ChronoUnit.SECONDS))
        ConsumerRecord<String, String> firstRecord = KafkaTestUtils.getSingleRecord(outputConsumer, OUTPUT_TOPIC, 30_000)

        then:
//        consumerRecords != null
//        !consumerRecords.empty
//        ConsumerRecord<String, String> firstRecord = consumerRecords.first()
        firstRecord.key() == "${evo.number1}"
        firstRecord.value() == "${evo.name} ${evo.number2}"
        noExceptionThrown()
    }

    def 'cleanup'() {
        //Shutting these down here makes test run faster :)
        avroProducer.close()
        kafkaListenerEndpointRegistry.getListenerContainer('avroConsumer').stop()
        outputProducer.close()
        outputConsumer.close()
    }

    def 'cleanupSpec'() {
        embeddedKafkaRule.after()
    }

}
