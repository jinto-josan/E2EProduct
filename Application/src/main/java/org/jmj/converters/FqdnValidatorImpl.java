package org.jmj.converters;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.jmj.annotations.FqdnValidator;
import org.jmj.entity.Response;
import org.jmj.entity.ResponseType;

public class FqdnValidatorImpl implements ConstraintValidator<FqdnValidator, Response> {

    @Override
    public boolean isValid(Response response, ConstraintValidatorContext context) {
        if (response.getType() == ResponseType.EVENT_HUB) {
            return response.getFqdn() != null && !response.getFqdn().isEmpty() && response.getFqdn().matches("^[a-zA-Z0-9.-]+/[a-zA-Z-]{2,}$");
        }
        return true;
    }
}
