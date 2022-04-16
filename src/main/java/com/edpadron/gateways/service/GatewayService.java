package com.edpadron.gateways.service;

import com.edpadron.gateways.entity.Gateway;
import com.edpadron.gateways.repository.GatewayRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class GatewayService {

    private static final Logger logger = LogManager.getLogger(GatewayService.class);

    @Autowired
    private GatewayRepository gatewayRepository;

    public Gateway addGateway(Gateway gateway){
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
