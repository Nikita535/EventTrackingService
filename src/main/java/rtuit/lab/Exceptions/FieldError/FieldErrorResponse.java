package rtuit.lab.Exceptions.FieldError;

import java.util.List;

public class FieldErrorResponse {

    private List<CustomFieldError> fieldErrors;


    public FieldErrorResponse() {
    }

    /**
     *
     * @return
     */
    public List<CustomFieldError> getFieldErrors() {
        return fieldErrors;
    }

    /**
     *
     * @param fieldErrors
     */
    public void setFieldErrors(List<CustomFieldError> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }
}
