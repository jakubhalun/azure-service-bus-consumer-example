package pl.halun.examples.servicebusconsumer.client;

import com.google.gson.Gson;
import com.microsoft.azure.servicebus.ReceiveMode;
import com.microsoft.azure.servicebus.SubscriptionClient;
import com.microsoft.azure.servicebus.primitives.ConnectionStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.halun.examples.servicebusconsumer.configuration.ServiceBusConfiguration;

import javax.annotation.PostConstruct;

@Service
public class ServiceBusTopicClient {
    private static final Gson GSON = new Gson();

    private ServiceBusConfiguration serviceBusConfiguration;

    @Autowired
    public ServiceBusTopicClient(ServiceBusConfiguration serviceBusConfiguration) {
        this.serviceBusConfiguration = serviceBusConfiguration;
    }

    @PostConstruct
    public void registerSubscriptionClient() throws Exception {
        SubscriptionClient subscription1Client = new SubscriptionClient(
                new ConnectionStringBuilder(
                        serviceBusConfiguration.getConnectionString(),
                        serviceBusConfiguration.getEntityPath()),
                ReceiveMode.PEEKLOCK);
    }
}
