package com.attijari.vocalbanking.exceptions;

import java.util.Date;

public class ClientNotFoundException extends RuntimeException {
    public ClientNotFoundException(String cin, String phoneNumber, Date birthday) {
        super("Client with CIN " + cin + " and birthday " + phoneNumber + " not found");
    }
}
