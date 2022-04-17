package com.edpadron.gateways.repository;

import com.edpadron.gateways.entity.Gateway;
import com.edpadron.gateways.entity.Peripheral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PeripheralRepository extends JpaRepository<Peripheral, Long> {
    Integer countAllByGateway(Gateway gateway);
    List<Peripheral> getAllByGateway(Gateway gateway);
}
