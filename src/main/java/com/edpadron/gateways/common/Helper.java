package com.edpadron.gateways.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class Helper {

    public ResponseEntity<Object> httpResponse(boolean success, Object data, HttpStatus httpStatus){
        return new ResponseEntity<>(new HashMap<Object, Object>(){{
            put("success", success);
            put("response", new HashMap<Object, Object>(){{
                put("data", data);
            }});
        }}, httpStatus);
    }
}
