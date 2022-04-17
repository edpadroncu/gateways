package com.edpadron.gateways.service;

import com.edpadron.gateways.common.Helper;
import com.edpadron.gateways.entity.Gateway;
import com.edpadron.gateways.exceptions.Ipv4Exception;
import com.edpadron.gateways.repository.GatewayRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
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
    private Helper helper;

    public GatewayService(GatewayRepository gatewayRepository) {
        this.gatewayRepository = gatewayRepository;
    }

    //get
    public GatewayRepository getGatewayRepository() {
        if (gatewayRepository == null)
            this.gatewayRepository = gt;
        return this.gatewayRepository;
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
        return helper.gatewayToMap(gateway);
    }

    public List<Map<String, Object>> getAllGatewaysDetails(){
        return helper.gatewayListToMap(getGatewayRepository().findAll());
    }

}
