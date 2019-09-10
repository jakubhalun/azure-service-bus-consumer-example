package pl.halun.examples.servicebusconsumer.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "azure.servicebus")
@Component
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

    public void setConnectionString(String connectionString) {
        this.connectionString = connectionString;
    }

    public void setEntityPath(String entityPath) {
        this.entityPath = entityPath;
    }

    public void setSessionEnabled(Boolean sessionEnabled) {
        this.sessionEnabled = sessionEnabled;
    }
}
