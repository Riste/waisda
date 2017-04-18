package nl.waisda.controllers.api;

import nl.waisda.domain.User;
import nl.waisda.services.UserSessionService;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.*;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract base controller from which other concrete REST API controllers can inherit. It provides methods that perform
 * authorization, validation, to-string-serialization, and error handling.
 */
abstract public class AbstractAPIController {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    private UserSessionService userSessionService;

    /**
     * Checks if authenticated user is admin.
     * @throws AuthenticationException if user is not authenticated
     * @throws AuthorizationException if user is authenticated but not admin
     */
    protected void checkIfAdmin() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        if (attributes != null) {
            User user = userSessionService.getCurrentUser(attributes.getRequest().getSession());
            if(user == null)
                throw new AuthenticationException();
            if (Boolean.FALSE.equals(user.getAdmin()))
                throw new AuthorizationException();
        } else
            throw new AuthenticationException();
    }

    /**
     * Checks the validity of the object provided as an argument.
     * @param validator used for validation
     * @param object that is validated
     * @param objectName Object name
     */
    protected void checkValid(Validator validator, Object object, String objectName) {
        BindingResult errors = new MapBindingResult(new HashMap(), objectName);
        validator.validate(object, errors);
        if(errors.hasErrors())
            throw new ValidationException(errors);
    }

    /**
     * Serializes provided object to json string representation.
     * @param object to be serialized
     * @return json string representation
     */
    protected String serialize(Object object){
        try {
            return MAPPER.writeValueAsString(object);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public String authenticationError(){
        return "Not authenticated!";
    }

    @ExceptionHandler(AuthorizationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public String authorizationError(){
        return "Not authrorized!";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String illegalArgumentException(IllegalArgumentException ex){
        return ex.getMessage();
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String validationError(ValidationException ex){
        Map<String, Map<String, String>> result = new HashMap<String, Map<String, String>>(1);
        result.put("errors", ex.getErrors());
        return serialize(result);
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public void exception(){}


    private static class AuthenticationException extends RuntimeException {}

    private static class AuthorizationException extends RuntimeException {}

    private static class ValidationException extends RuntimeException{
        private final Map<String, String> errors;

        public ValidationException(Errors errors) {
            Map<String, String> errorsMap = new HashMap<String, String>(errors.getErrorCount());
            for(Object errorObj: errors.getFieldErrors()) {
                FieldError error = (FieldError)errorObj;
                errorsMap.put(error.getField(), error.getDefaultMessage());
            }
            this.errors = Collections.unmodifiableMap(errorsMap);
        }

        public Map<String, String> getErrors() {
            return errors;
        }
    }

}
