package com.edpadron.gateways.repository;

import com.edpadron.gateways.entity.Gateway;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GatewayRepository extends JpaRepository<Gateway, Long> {

}
