package pkgggg

import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.listener.KafkaListenerErrorHandler
import org.springframework.kafka.listener.ListenerExecutionFailedException
import org.springframework.messaging.Message
import org.springframework.messaging.support.GenericMessage
import org.springframework.stereotype.Component

@Component
class CustomHandler implements KafkaListenerErrorHandler {

    public static Logger logger = LoggerFactory.getLogger(CustomHandler.class);

    @Override
    Object handleError(Message<?> message, ListenerExecutionFailedException exception) {
        logger.info('BAD MESSAGE:  ' + message?.payload)
        return null
    }

    @Override
    Object handleError(Message<?> message, ListenerExecutionFailedException exception, Consumer<?, ?> consumer) {
        logger.error('BAD MESSAGE:  ' + message?.payload)
        return null
    }

}
