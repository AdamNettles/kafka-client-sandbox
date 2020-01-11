package pkgggg

import groovy.util.logging.Slf4j
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.listener.ContainerAwareErrorHandler
import org.springframework.kafka.listener.MessageListenerContainer
import org.springframework.kafka.support.serializer.DeserializationException
import org.springframework.stereotype.Component

@Component
@Slf4j
class ContainerErrorHandler implements ContainerAwareErrorHandler {

    @Override
    void handle(Exception thrownException, ConsumerRecord<?, ?> data) {
        log.error('ContainerErrorHandler 1')
    }

    @Override
    void handle(Exception thrownException, ConsumerRecord<?, ?> data, Consumer<?, ?> consumer) {
        log.error('ContainerErrorHandler 2')
    }

    @Override
    void clearThreadState() {
//        super.clearThreadState()
    }

    @Override
    boolean isAckAfterHandle() {
//        return super.isAckAfterHandle()
        false
    }

    @Override
    void handle(Exception thrownException, List<ConsumerRecord<?, ?>> records, Consumer<?, ?> consumer) {
        log.error('ContainerErrorHandler 3')
    }

    @Override
    void handle(Exception thrownException, List<ConsumerRecord<?, ?>> records, Consumer<?, ?> consumer, MessageListenerContainer container) {
        log.error('ContainerErrorHandler 4')
        if(thrownException.cause.hasProperty('failedMessage')){
            byte[] deserializerExceptionBytes = thrownException.cause.failedMessage.headers.get('springDeserializerExceptionValue')
            if(deserializerExceptionBytes != null) {
                DeserializationException dse = toObject(deserializerExceptionBytes) as DeserializationException
                Throwable rootCause = getRootCause(dse)
                log.error('Root cause ' + rootCause.message)
                if(rootCause.message == 'Unknown magic byte!') {
                    log.error('The message likely was not produced by an avro producer following schema registry')
                }
            }
        } else {
            log.error(thrownException.toString())
        }

    }

    private static Object toObject(byte[] bytes) {
        Object obj = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bis);
            obj = ois.readObject();
        } catch (Exception ex) {
            ex.printStackTrace(); // ClassNotFoundException
        }
        return obj;
    }

    Throwable getRootCause(DeserializationException dse) {
        def cause = dse.cause
        while(cause.cause != null && cause.cause != cause) {
            cause = cause.cause
        }
        return cause
    }

}

