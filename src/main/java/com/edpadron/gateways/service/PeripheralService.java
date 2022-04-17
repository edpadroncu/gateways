package com.edpadron.gateways.service;

import com.edpadron.gateways.common.Helper;
import com.edpadron.gateways.entity.Gateway;
import com.edpadron.gateways.entity.Peripheral;
import com.edpadron.gateways.exceptions.CustomException;
import com.edpadron.gateways.exceptions.StatusException;
import com.edpadron.gateways.exceptions.UidException;
import com.edpadron.gateways.repository.PeripheralRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PeripheralService {

    private static final Logger logger = LogManager.getLogger(PeripheralService.class);

    @Autowired
    private PeripheralRepository peripheralRepository;

    @Autowired
    private GatewayService gatewayService;

    @Autowired
    private Helper helper;

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
            throw new ValidationException("Peripheral id not found: " + id);
        }
        return "Deleted";
    }

    public Peripheral getPeripheralById(Long id){
        Optional<Peripheral> peripheral = peripheralRepository.findById(id);
        if (!peripheral.isPresent())
            throw new ValidationException("Peripheral id not found: " + id);
        return peripheral.get();
    }

    public Map<String, Object> addPerToGtw(Long peripheralId, Long gatewayId){
        Peripheral peripheral = getPeripheralById(peripheralId);
        Gateway gateway = gatewayService.getGatewayById(gatewayId);
        if (peripheralRepository.countAllByGateway(gateway) >= 10)
            throw new CustomException("No more than 10 peripheral devices are allowed for a gateway");
        peripheral.setGateway(gateway);
        Peripheral saved = peripheralRepository.save(peripheral);
        return helper.peripheralToMap(saved);
    }

    public Map<String, Object> removePerFromGtw(Long peripheralId){
        Peripheral peripheral = getPeripheralById(peripheralId);
        peripheral.setGateway(null);
        Peripheral saved = peripheralRepository.save(peripheral);
        return helper.peripheralToMap(saved);
    }

    public List<Peripheral> peripheralsByGateway(Gateway gateway){
        return peripheralRepository.getAllByGateway(gateway);
    }

}
