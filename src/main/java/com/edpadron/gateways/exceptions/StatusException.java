package com.edpadron.gateways.exceptions;

public class StatusException extends RuntimeException {
    public StatusException() {
        super("Invalid status value, allowed only (online, offline)");
    }
}
