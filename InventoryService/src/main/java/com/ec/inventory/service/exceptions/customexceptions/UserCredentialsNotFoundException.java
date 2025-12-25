package com.ec.inventory.service.exceptions.customexceptions;

public class UserCredentialsNotFoundException extends RuntimeException {
    public UserCredentialsNotFoundException(String message) {
        super(message);
    }
}
