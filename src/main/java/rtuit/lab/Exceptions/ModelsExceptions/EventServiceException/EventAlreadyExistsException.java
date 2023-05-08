package rtuit.lab.Exceptions.ModelsExceptions.EventServiceException;

public class EventAlreadyExistsException extends EventServiceExceptions{
    public EventAlreadyExistsException(String message) {
        super(message, "Event already exists :(");
    }
}
