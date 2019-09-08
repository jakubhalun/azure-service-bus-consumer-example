package pl.halun.examples.servicebusconsumer.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "azure.servicebus")
public class ServiceBusConfiguration {

    private String connectionString;
    private String entityPath;
    private Boolean sessionEnabled;

    public String getConnectionString() {
        return connectionString;
    }

    public String getEntityPath() {
        return entityPath;
    }

    public Boolean getSessionEnabled() {
        return sessionEnabled;
    }
}
