package com.edpadron.gateways.service;

import com.edpadron.gateways.common.Helper;
import com.edpadron.gateways.entity.Gateway;
import com.edpadron.gateways.exceptions.Ipv4Exception;
import com.edpadron.gateways.repository.GatewayRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@NoArgsConstructor
@Service
public class GatewayService {

    @Autowired
    private GatewayRepository gt;
    private GatewayRepository gatewayRepository;

    @Autowired
    private PeripheralService ps;
    private PeripheralService peripheralService;

    public GatewayService(GatewayRepository gatewayRepository, PeripheralService peripheralService) {
        this.gatewayRepository = gatewayRepository;
        this.peripheralService = peripheralService;
    }

    public GatewayService(GatewayRepository gatewayRepository) {
        this.gatewayRepository = gatewayRepository;
    }

    //get
    public GatewayRepository getGatewayRepository() {
        if (this.gatewayRepository == null)
            this.gatewayRepository = gt;
        return this.gatewayRepository;
    }

    //get
    public PeripheralService getPeripheralService() {
        if (this.peripheralService == null)
            this.peripheralService = ps;
        return this.peripheralService;
    }

    public Gateway addGateway(Gateway gateway){
        if (!gateway.isValidIpv4())
            throw new Ipv4Exception();
        return getGatewayRepository().save(gateway);
    }

    public List<Gateway> getAllGateways(){
        return getGatewayRepository().findAll();
    }

    public String deleteGatewayById(Long id){
        try {
            getGatewayRepository().deleteById(id);
        }
        catch (EmptyResultDataAccessException ex){
            throw new ValidationException("Gateway id not found: " + id);
        }
        return "Deleted";
    }

    public Gateway getGatewayById(Long id){
        Optional<Gateway> gateway = getGatewayRepository().findById(id);
        if (!gateway.isPresent())
            throw new ValidationException("Gateway id not found: " + id);
        return gateway.get();
    }

    public Map<String, Object> getGatewayDetailsById(Long id){
        Gateway gateway = getGatewayById(id);
        return gatewayToMap(gateway);
    }

    public List<Map<String, Object>> getAllGatewaysDetails(){
        return gatewayListToMap(getGatewayRepository().findAll());
    }

    public Map<String, Object> gatewayToMap(Gateway gateway){
        Map<String, Object> objectMap = Helper.mapper().convertValue(gateway, Map.class);
        objectMap.put("peripherals", getPeripheralService().peripheralsByGateway(gateway));
        return objectMap;
    }

    private List<Map<String, Object>> gatewayListToMap(List<Gateway> gateways){
        List<Map<String, Object>> list = new ArrayList<>();
        for (Gateway gateway: gateways) {
            Map<String, Object> objectMap = Helper.mapper().convertValue(gateway, Map.class);
            objectMap.put("peripherals", getPeripheralService().peripheralsByGateway(gateway));
            list.add(objectMap);
        }
        return list;
    }

}
