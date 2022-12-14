package pe.edu.upc.banking.transactions.command.application.handlers;

import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Component
public class LoggingEventHandler {
	@EventHandler
    public void on(Object event) {
        System.out.println("Event received: " + event.toString());
    }
}
