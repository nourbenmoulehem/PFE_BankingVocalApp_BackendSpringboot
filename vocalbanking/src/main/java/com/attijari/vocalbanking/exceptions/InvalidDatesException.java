package com.attijari.vocalbanking.exceptions;

import java.util.Date;

public class InvalidDatesException extends RuntimeException {
    public InvalidDatesException(String message) {
        super(message);
    }
}