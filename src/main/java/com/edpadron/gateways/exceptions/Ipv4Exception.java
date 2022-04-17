package com.edpadron.gateways.exceptions;

public class Ipv4Exception extends RuntimeException {
    public Ipv4Exception() {
        super("Invalid ipv4 address");
    }
}
