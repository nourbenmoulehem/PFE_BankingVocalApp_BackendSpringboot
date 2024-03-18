package com.attijari.vocalbanking.exceptions;

public class TokenExpiredException extends RuntimeException {
    public TokenExpiredException() {
        super("Le jeton a expiré");
    }
}
