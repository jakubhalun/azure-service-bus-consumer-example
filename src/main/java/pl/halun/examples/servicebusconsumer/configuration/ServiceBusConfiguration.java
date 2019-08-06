package pl.halun.examples.servicebusconsumer.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceBusConfiguration {

    @Value("${azure.servicebus.connection.string}")
    private String connectionString;

    @Value("${azure.servicebus.entity.path}")
    private String entityPath;

    public String getConnectionString() {
        return connectionString;
    }

    public String getEntityPath() {
        return entityPath;
    }
}
