package com.edpadron.gateways.service;

import com.edpadron.gateways.common.Helper;
import com.edpadron.gateways.entity.Gateway;
import com.edpadron.gateways.exceptions.Ipa4Exception;
import com.edpadron.gateways.repository.GatewayRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class GatewayService {

    private static final Logger logger = LogManager.getLogger(GatewayService.class);

    @Autowired
    private GatewayRepository gatewayRepository;

    @Autowired
    private Helper helper;

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
            throw new ValidationException("Gateway id not found: " + id);
        }
        return "Deleted";
    }

    public Gateway getGatewayById(Long id){
        Optional<Gateway> gateway = gatewayRepository.findById(id);
        if (!gateway.isPresent())
            throw new ValidationException("Gateway id not found: " + id);
        return gateway.get();
    }

    public Map<String, Object> getGatewayDetailsById(Long id){
        return helper.gatewayToMap(getGatewayById(id));
    }

    public List<Map<String, Object>> getAllGatewaysDetails(){
        List<Gateway> gatewayslist = gatewayRepository.findAll();
        return helper.gatewayListToMap(gatewayRepository.findAll());

//        List<UserLazy> users = sessionLazy.createQuery("From UserLazy").list();
//        UserLazy userLazyLoaded = users.get(3);
//        return (userLazyLoaded.getOrderDetail());
    }

}
