package com.edpadron.gateways.repository;

import com.edpadron.gateways.entity.Gateway;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GatewayRepository extends JpaRepository<Gateway, Long> {
////    @Query("select from Gateway left join fetch article.topics where article.id =:id")
//    @Query(value = "select g from gateway g inner join fetch g.peripherals", nativeQuery = true)
//    List<Gateway> listall();

//    select cal from ContactAddressLink cal
//    inner join fetch cal.contact c
//    inner join fetch cal.address a
//    where cal.id = 123456789
}


