package rtuit.lab.Exceptions.ModelsExceptions.EventServiceException;

public class EventNotFoundException extends EventServiceExceptions {
    public EventNotFoundException(String message) {
        super(message, "Event not found :(");
    }
}