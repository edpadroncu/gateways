package com.edpadron.gateways.service;

import com.edpadron.gateways.entity.Peripheral;
import com.edpadron.gateways.exceptions.ApiError;
import com.edpadron.gateways.exceptions.StatusException;
import com.edpadron.gateways.exceptions.UidException;
import com.edpadron.gateways.repository.PeripheralRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PeripheralService {

    private static final Logger logger = LogManager.getLogger(PeripheralService.class);

    @Autowired
    private PeripheralRepository peripheralRepository;

    public Peripheral addPeripheral(Peripheral peripheral){
        if (!peripheral.isValidStatus())
            throw new StatusException();

        if (!peripheral.isValidUid())
            throw new UidException();

        peripheral.setCreated_at(LocalDateTime.now());
        return peripheralRepository.save(peripheral);
    }

    public List<Peripheral> getAllPeripheral(){
        return peripheralRepository.findAll();
    }

    public String deletePeripheralById(Long id){
        try {
            peripheralRepository.deleteById(id);
        }
        catch (EmptyResultDataAccessException ex){
            throw new ValidationException("Id value not found: " + id);
        }
        return "Deleted";
    }

    public Peripheral getPeripheralById(Long id){
        Optional<Peripheral> medication = peripheralRepository.findById(id);
        if (!medication.isPresent())
            throw new ValidationException("Id value not found: " + id);
        return medication.get();
    }
}
