package wheelsonthebus.users.config;

import org.springframework.cloud.bus.jackson.RemoteApplicationEventScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@RemoteApplicationEventScan(basePackages = "wheelsonthebus.tools.events")
public class BusConfiguration {
    // According to documentation, this should register event with the deserializer
    // Source : https://cloud.spring.io/spring-cloud-bus/single/spring-cloud-bus.html#_registering_events_in_custom_packages
}