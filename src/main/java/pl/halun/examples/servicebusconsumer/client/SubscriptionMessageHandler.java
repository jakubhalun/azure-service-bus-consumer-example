package pl.halun.examples.servicebusconsumer.client;

import com.microsoft.azure.servicebus.ExceptionPhase;
import com.microsoft.azure.servicebus.IMessage;
import com.microsoft.azure.servicebus.IMessageHandler;
import com.microsoft.azure.servicebus.MessageHandlerOptions;
import com.microsoft.azure.servicebus.primitives.ServiceBusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import pl.halun.examples.servicebusconsumer.configuration.ServiceBusConfiguration;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

@Service
@ConditionalOnProperty(
        value="azure.servicebus.sessionEnabled",
        havingValue = "false"
)
public class SubscriptionMessageHandler extends SubscriptionHandler implements IMessageHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(SubscriptionMessageHandler.class);

    @Autowired
    public SubscriptionMessageHandler(ServiceBusConfiguration serviceBusConfiguration) throws ServiceBusException,
            InterruptedException {
        super(serviceBusConfiguration);
    }

    @PostConstruct
    private void registerMessageHandler() throws Exception {
        subscriptionClient.registerMessageHandler(this,
                new MessageHandlerOptions(1, false, Duration.ofMinutes(1)),
                Executors.newSingleThreadExecutor());
        LOGGER.info("Registered basic message handler. No session");
    }

    @Override
    public CompletableFuture<Void> onMessageAsync(IMessage message) {
        logMessage(message);
        return subscriptionClient.completeAsync(message.getLockToken());
    }

    @Override
    public void notifyException(Throwable exception, ExceptionPhase phase) {
        logError(exception, phase);
    }
}
