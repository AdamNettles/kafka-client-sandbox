package pkgggg

import org.apache.kafka.clients.consumer.Consumer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.listener.KafkaListenerErrorHandler
import org.springframework.kafka.listener.ListenerExecutionFailedException
import org.springframework.messaging.Message
import org.springframework.stereotype.Component

@Component
class CustomHandler implements KafkaListenerErrorHandler {

    public static Logger logger = LoggerFactory.getLogger(CustomHandler.class);

    @Override
    Object handleError(Message<?> message, ListenerExecutionFailedException exception) {
        logger.info('ERROR HANDLED')
        return null
    }

    @Override
    Object handleError(Message<?> message, ListenerExecutionFailedException exception, Consumer<?, ?> consumer) {
        logger.info('ERROR HANDLED2')
        return super.handleError(message, exception, consumer)
    }
}
