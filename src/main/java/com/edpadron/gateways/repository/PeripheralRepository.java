package com.edpadron.gateways.repository;

import com.edpadron.gateways.entity.Peripheral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PeripheralRepository extends JpaRepository<Peripheral, Long> {

}
