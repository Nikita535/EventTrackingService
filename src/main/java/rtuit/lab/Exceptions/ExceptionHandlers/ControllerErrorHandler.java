package rtuit.lab.Exceptions.ExceptionHandlers;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import rtuit.lab.Exceptions.FieldError.CustomFieldError;
import rtuit.lab.Exceptions.FieldError.FieldErrorResponse;
import rtuit.lab.Exceptions.ModelsExceptions.EventServiceException.EventServiceExceptions;
import rtuit.lab.Exceptions.ModelsExceptions.ExceptionWrapper;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ControllerErrorHandler extends ResponseEntityExceptionHandler {


    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        FieldErrorResponse fieldErrorResponse = new FieldErrorResponse();

        List<CustomFieldError> fieldErrors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            CustomFieldError fieldError = new CustomFieldError();
            fieldError.setField(((FieldError) error).getField());
            fieldError.setMessage(error.getDefaultMessage());
            fieldErrors.add(fieldError);
        });


        fieldErrorResponse.setFieldErrors(fieldErrors);
        return new ResponseEntity<>(fieldErrorResponse, status);
    }

    @ExceptionHandler(EventServiceExceptions.class)
    public ResponseEntity<?> handleException(ExceptionWrapper ex) {
        return ResponseEntity
                .badRequest()
                .body(new ExceptionResponse(ex.getCode(), ex.getMessage()));
    }

    @JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
    @JsonTypeName("error")
    public record ExceptionResponse(String code, String message) {
    }
}