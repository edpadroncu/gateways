package com.edpadron.gateways.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class Helper {

    public static ResponseEntity<Object> httpResponse(boolean success, Object data, HttpStatus httpStatus){
        return new ResponseEntity<>(new HashMap<Object, Object>(){{
            put("success", success);
            put("response", new HashMap<Object, Object>(){{
                put("data", data);
            }});
        }}, httpStatus);
    }

    public static ObjectMapper mapper(){
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        return mapper;
    }
}
