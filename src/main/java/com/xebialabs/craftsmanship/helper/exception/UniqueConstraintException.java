package com.xebialabs.craftsmanship.helper.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = "Please try with different player information")
public class UniqueConstraintException extends Exception
{
    static final long serialVersionUID = -3387516993334229948L;


    public UniqueConstraintException(String message)
    {
        super(message);
    }

}