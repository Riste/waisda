package nl.waisda.validators;

import nl.waisda.domain.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.validation.ConstraintViolation;
import java.util.Set;

/**
 * Created by risto on 14.04.17.
 */
@Component
public class VideoValidator implements Validator{

    @Autowired
    private javax.validation.Validator validator;

    @Override
    public boolean supports(Class<?> aClass) {
        return Video.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        //JSR303 validation
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(o);
        for (ConstraintViolation constraintViolation: constraintViolations){
            String propertyPath = constraintViolation.getPropertyPath().toString();
            String message = constraintViolation.getMessage();
            errors.rejectValue(propertyPath, "", message);
        }
    }
}
