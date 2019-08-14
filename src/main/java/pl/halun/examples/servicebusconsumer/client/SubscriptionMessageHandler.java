package pl.halun.examples.servicebusconsumer.client;

import com.microsoft.azure.servicebus.*;
import com.microsoft.azure.servicebus.primitives.ConnectionStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.halun.examples.servicebusconsumer.configuration.ServiceBusConfiguration;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
public class SubscriptionMessageHandler implements IMessageHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(SubscriptionMessageHandler.class);

    private SubscriptionClient subscriptionClient;

    @Autowired
    public SubscriptionMessageHandler(ServiceBusConfiguration serviceBusConfiguration) throws Exception {
        subscriptionClient = new SubscriptionClient(new ConnectionStringBuilder(
                serviceBusConfiguration.getConnectionString(),
                serviceBusConfiguration.getEntityPath()), ReceiveMode.PEEKLOCK);
    }

    @PostConstruct
    private void registerMessaageHandler() throws Exception {
        subscriptionClient.registerMessageHandler(this,
                new MessageHandlerOptions(1, false, Duration.ofMinutes(1)),
                Executors.newSingleThreadExecutor());
    }

    @Override
    public CompletableFuture<Void> onMessageAsync(IMessage message) {
        LOGGER.info("Message Id: {}, Sequence number: {}, EnqueuedTimeUtc: {}, ExpiresAtUtc: {}, ContentType: {}",
                message.getMessageId(), message.getSequenceNumber(), message.getEnqueuedTimeUtc(),
                message.getExpiresAtUtc(), message.getContentType());

        List<byte[]> bodyList = message.getMessageBody().getBinaryData();
        bodyList.forEach(this::logContent);

        return subscriptionClient.completeAsync(message.getLockToken());
    }

    private void logContent(byte[] body) {
        String content = new String(body, UTF_8);
        LOGGER.info("Content: {}", content);
    }

    @Override
    public void notifyException(Throwable exception, ExceptionPhase phase) {
        LOGGER.error("{} - {}", phase.toString(), exception.getMessage());
    }
}
