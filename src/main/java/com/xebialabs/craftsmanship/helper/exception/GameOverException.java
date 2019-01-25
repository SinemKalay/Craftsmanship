package com.xebialabs.craftsmanship.helper.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE, reason = "Game over! Could not shot anymore!")
public class GameOverException extends Exception
{
    static final long serialVersionUID = -3387516993334229948L;


    public GameOverException(String message)
    {
        super(message);
    }

}
