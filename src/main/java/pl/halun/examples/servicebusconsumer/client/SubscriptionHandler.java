package pl.halun.examples.servicebusconsumer.client;

import com.microsoft.azure.servicebus.ExceptionPhase;
import com.microsoft.azure.servicebus.IMessage;
import com.microsoft.azure.servicebus.ReceiveMode;
import com.microsoft.azure.servicebus.SubscriptionClient;
import com.microsoft.azure.servicebus.primitives.ConnectionStringBuilder;
import com.microsoft.azure.servicebus.primitives.ServiceBusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.halun.examples.servicebusconsumer.configuration.ServiceBusConfiguration;

import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

public abstract class SubscriptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(SubscriptionHandler.class);

    protected final SubscriptionClient subscriptionClient;

    protected SubscriptionHandler(ServiceBusConfiguration serviceBusConfiguration) throws ServiceBusException, InterruptedException {
        subscriptionClient = new SubscriptionClient(new ConnectionStringBuilder(
                serviceBusConfiguration.getConnectionString(),
                serviceBusConfiguration.getEntityPath()), ReceiveMode.PEEKLOCK);
    }

    protected void logMessage(IMessage message) {
        LOGGER.info("Message Id: {}, Sequence number: {}, EnqueuedTimeUtc: {}, ExpiresAtUtc: {}, ContentType: {}",
                message.getMessageId(), message.getSequenceNumber(), message.getEnqueuedTimeUtc(),
                message.getExpiresAtUtc(), message.getContentType());

        List<byte[]> bodyList = message.getMessageBody().getBinaryData();
        bodyList.forEach(this::logMessageContent);
    }

    protected void logError(Throwable exception, ExceptionPhase phase) {
        LOGGER.error("{} - {}", phase.toString(), exception.getMessage());
    }

    private void logMessageContent(byte[] body) {
        String content = new String(body, UTF_8);
        LOGGER.info("Content: {}", content);
    }
}
