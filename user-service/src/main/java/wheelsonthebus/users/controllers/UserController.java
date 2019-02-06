package wheelsonthebus.users.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.bus.ServiceMatcher;
import org.springframework.cloud.bus.jackson.RemoteApplicationEventScan;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import wheelsonthebus.users.borrowed.BusJacksonMessageConverter;
import wheelsonthebus.users.events.NameEvent;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@EnableAutoConfiguration
@SpringBootConfiguration
@RemoteApplicationEventScan(basePackages = "wheelsonthebus.users.events")
public class UserController {

    final private Set<String> names = ConcurrentHashMap.newKeySet();

    @Autowired
    private BusJacksonMessageConverter converter; //TODO Find out how it should be done

    @Autowired
    private ServiceMatcher busServiceMatcher;

    @Autowired
    private ApplicationEventPublisher publisher;

    @GetMapping("/names")
    public Collection<String> names() {
        return this.names;
    }

    @PutMapping("/names/{name}")
    public void sayName(@PathVariable String name) {
        this.names.add(name);
        this.publisher.publishEvent(
                new NameEvent(this, this.busServiceMatcher.getServiceId(), name));
    }

    @EventListener
    public void handleNameSaid(NameEvent event) {
        this.names.add(event.getName());
    }
}
