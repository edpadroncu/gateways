package com.edpadron.gateways.repository;

import com.edpadron.gateways.entity.Gateway;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GatewayRepository extends JpaRepository<Gateway, Long> {

}


