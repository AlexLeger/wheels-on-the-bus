package wheelsonthebus.users.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.bus.ServiceMatcher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import wheelsonthebus.tools.events.NameEvent;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@EnableAutoConfiguration
@SpringBootConfiguration
@Slf4j
public class UserController {

    final private Set<String> names = ConcurrentHashMap.newKeySet();

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
        NameEvent event = new NameEvent(this, this.busServiceMatcher.getServiceId(), name);
        this.publisher.publishEvent(event);
        log.info("Event was published for name "+event.getName()+
                " with id "+event.getId()+
                " by source "+event.getSource()+
                " from originService "+event.getOriginService()+"" +
                " with destinationService "+event.getDestinationService());
    }
    
    @EventListener
    public void handleNameSaid(NameEvent event) {
        this.names.add(event.getName());
        log.info("Event was received for name "+event.getName()+
                " with id "+event.getId()+
                " by source "+event.getSource()+
                " from originService "+event.getOriginService()+"" +
                " with destinationService "+event.getDestinationService());
    }
}
