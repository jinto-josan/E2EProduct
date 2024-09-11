package org.jmj.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.jmj.converters.FqdnValidatorImpl;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = FqdnValidatorImpl.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface FqdnValidator {
    String message() default "FQDN must be present if responseId.type is EVENTHUB and match the pattern ^[a-zA-Z0-9.-]+/[a-zA-Z-]{2,}$";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
