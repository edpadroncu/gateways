package com.edpadron.gateways.exceptions;

public class UidException extends RuntimeException {
    public UidException() {
        super("Invalid uid value, allowed only numbers");
    }
}
