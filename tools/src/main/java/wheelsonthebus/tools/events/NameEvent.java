package wheelsonthebus.tools.events;

import org.springframework.cloud.bus.event.RemoteApplicationEvent;

public class NameEvent extends RemoteApplicationEvent {

    private String name;

    protected NameEvent() {
    }

    public NameEvent(Object source, String originService, String name) {
        super(source, originService);
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
