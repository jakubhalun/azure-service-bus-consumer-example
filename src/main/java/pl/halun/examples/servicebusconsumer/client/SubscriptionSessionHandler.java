package pl.halun.examples.servicebusconsumer.client;

import com.microsoft.azure.servicebus.*;
import com.microsoft.azure.servicebus.primitives.ServiceBusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
        havingValue = "true",
        matchIfMissing = true
)
public class SubscriptionSessionHandler extends SubscriptionHandler implements ISessionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(SubscriptionSessionHandler.class);

    public SubscriptionSessionHandler(ServiceBusConfiguration serviceBusConfiguration) throws ServiceBusException,
            InterruptedException {
        super(serviceBusConfiguration);
    }

    @PostConstruct
    private void registerSessionMessageHandler() throws Exception {
        subscriptionClient.registerSessionHandler(this,
                new SessionHandlerOptions(4, false, Duration.ofMinutes(1)),
                Executors.newSingleThreadExecutor());
        LOGGER.info("Registered session message handler.");
    }

    @Override
    public CompletableFuture<Void> onMessageAsync(IMessageSession session, IMessage message) {
        logMessage(message);
        return session.completeAsync(message.getLockToken());
    }

    @Override
    public CompletableFuture<Void> OnCloseSessionAsync(IMessageSession session) {
        LOGGER.info("Session with ID {} closed", session.getSessionId());
        return null;
    }

    @Override
    public void notifyException(Throwable exception, ExceptionPhase phase) {
        logError(exception, phase);
    }
}
