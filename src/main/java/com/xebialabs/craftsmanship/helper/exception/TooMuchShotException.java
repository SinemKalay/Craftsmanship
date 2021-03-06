package com.xebialabs.craftsmanship.helper.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = "Number of shots can not be more than spaceships you have!")
public class TooMuchShotException extends Exception
{
    static final long serialVersionUID = -3387516993334229948L;


    public TooMuchShotException(String message)
    {
        super(message);
    }

}