# Azure Service Bus Consumer
This project is a basic example of Java Azure Service Bus Consumer, based on [Microsoft's documentation](https://docs.microsoft.com/pl-pl/azure/service-bus-messaging/service-bus-java-how-to-use-queues) and running on a Spring Boot.

### Requirements
Java 8, Gradle

### Usage
* Fill the *application.properties* file with a connection string to a Service Bus and entity path.
  * Format of entity path is *topicName/subscriptions/subscriptionName*
* Run **gradle bootRun**



