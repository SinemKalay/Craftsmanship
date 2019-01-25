package com.xebialabs.craftsmanship.helper.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Could not find user with id.")
public class NotFoundUserException extends Exception
{
    static final long serialVersionUID = -3387516993334229948L;

    public NotFoundUserException(String message) {
        super(message);
    }

}