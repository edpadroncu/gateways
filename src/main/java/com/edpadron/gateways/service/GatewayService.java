package com.edpadron.gateways.service;

import com.edpadron.gateways.entity.Gateway;
import com.edpadron.gateways.exceptions.Ipa4Exception;
import com.edpadron.gateways.repository.GatewayRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.util.*;

@Service
public class GatewayService {

    private static final Logger logger = LogManager.getLogger(GatewayService.class);

    @Autowired
    private GatewayRepository gatewayRepository;

    public Gateway addGateway(Gateway gateway){
        if (!gateway.isValidIpv4())
            throw new Ipa4Exception();
        return gatewayRepository.save(gateway);
    }

    public List<Gateway> getAllGateways(){
        return gatewayRepository.findAll();
    }

    public String deleteGatewayById(Long id){
        try {
            gatewayRepository.deleteById(id);
        }
        catch (EmptyResultDataAccessException ex){
            throw new ValidationException("Id value not found: " + id);
        }
        return "Deleted";
    }

    public Gateway getGatewayById(Long id){
        Optional<Gateway> medication = gatewayRepository.findById(id);
        if (!medication.isPresent())
            throw new ValidationException("Id value not found: " + id);
        return medication.get();
    }
}
