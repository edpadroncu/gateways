package com.edpadron.gateways.common;

import com.edpadron.gateways.entity.Gateway;
import com.edpadron.gateways.entity.Peripheral;
import com.edpadron.gateways.service.PeripheralService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class Helper {

    @Autowired
    PeripheralService peripheralService;

    public ResponseEntity<Object> httpResponse(boolean success, Object data, HttpStatus httpStatus){
        return new ResponseEntity<>(new HashMap<Object, Object>(){{
            put("success", success);
            put("response", new HashMap<Object, Object>(){{
                put("data", data);
            }});
        }}, httpStatus);
    }

    public Map<String, Object> peripheralToMap(Peripheral peripheral){
        Map<String, Object> objectMap = mapper().convertValue(peripheral, Map.class);
        Long a = peripheral.getGateway().getIdg();
        objectMap.put("gateway", peripheral.getGateway());
        return objectMap;
    }

    public Map<String, Object> gatewayToMap(Gateway gateway){
        Map<String, Object> objectMap = mapper().convertValue(gateway, Map.class);
        objectMap.put("peripherals", peripheralService.peripheralsByGateway(gateway));
        return objectMap;
    }

    public List<Map<String, Object>> gatewayListToMap(List<Gateway> gateways){
        List<Map<String, Object>> list = new ArrayList<>();
        for (Gateway gateway: gateways) {
            Map<String, Object> objectMap = mapper().convertValue(gateway, Map.class);
            objectMap.put("peripherals", peripheralService.peripheralsByGateway(gateway));
            list.add(objectMap);
        }
        return list;
    }

    private ObjectMapper mapper(){
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }
}
