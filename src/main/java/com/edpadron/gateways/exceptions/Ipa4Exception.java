package com.edpadron.gateways.exceptions;

public class Ipa4Exception extends RuntimeException {
    public Ipa4Exception() {
        super("Invalid ipv4 address");
    }
}
