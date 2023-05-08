package rtuit.lab.Exceptions.ModelsExceptions.EventServiceException;

public class PermissionDeniedException extends EventServiceExceptions{
    public PermissionDeniedException(String message) {
        super(message, "You cant delete strangers events :(");
    }
}
