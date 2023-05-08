package rtuit.lab.Exceptions.ModelsExceptions.EventServiceException;

public class UnusualException extends EventServiceExceptions{
    public UnusualException(String message) {
        super(message, "Something went wrong :O");
    }
}
