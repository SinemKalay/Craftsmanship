package com.xebialabs.craftsmanship.helper.exception;

import org.springframework.http.HttpStatus;
        import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Please wait for opponent move! It is not your turn.")
public class NotYourTurnException extends Exception
{
    static final long serialVersionUID = -3387516993334229948L;

    public NotYourTurnException(String message) {
        super(message);
    }

}