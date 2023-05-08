package rtuit.lab.Exceptions.ModelsExceptions.EventServiceException;

import rtuit.lab.Exceptions.ModelsExceptions.ExceptionWrapper;

public class EventServiceExceptions extends RuntimeException implements ExceptionWrapper {
    private final String code;
    public EventServiceExceptions(String message, String code) {
        super(message);
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}