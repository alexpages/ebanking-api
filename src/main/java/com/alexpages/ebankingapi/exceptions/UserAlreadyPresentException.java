package com.alexpages.ebankingapi.exceptions;

public class UserAlreadyPresentException extends RuntimeException{
    public UserAlreadyPresentException(String message) {
        super(message);
    }
}
