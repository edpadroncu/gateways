package com.edpadron.gateways.service;

import com.edpadron.gateways.entity.Gateway;
import com.edpadron.gateways.entity.Peripheral;
import com.edpadron.gateways.exceptions.Ipv4Exception;
import com.edpadron.gateways.repository.GatewayRepository;
import com.edpadron.gateways.repository.PeripheralRepository;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.ValidationException;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@DataJpaTest
class GatewayServiceTest {

    @Autowired
    private GatewayRepository gatewayRepository;

    @Autowired
    private PeripheralRepository peripheralRepository;

    private GatewayService gatewayService;

    @BeforeEach
    void setUp() {
        gatewayService = new GatewayService(gatewayRepository);
    }

    @Test
    void getAllGateways() {
        gatewayService.addGateway(new Gateway("SERIAL_NUM1", "NAME_A", "127.0.0.1"));
        gatewayService.addGateway(new Gateway("SERIAL_NUM2", "NAME_A", "127.0.0.1"));
        assertThat(gatewayService.getAllGateways().size()).isEqualTo(2);
    }

    @Test
    void addGateway() {
        Gateway gateway = gatewayService.addGateway(new Gateway("SERIAL_NUM1", "NAME_A", "127.0.0.1"));
        assertThat(gateway.getIdg()).isNotNull();
    }

    @Test
    void addGatewayThrowIpv4Exception(){
        Gateway gateway = new Gateway("SERIAL_NUM1", "NAME_A", "127.0.0");
        assertThatThrownBy(() -> gatewayService.addGateway(gateway)).isInstanceOf(Ipv4Exception.class);
    }

    @Test
    void deleteGatewayById() {
        Gateway gateway = gatewayService.addGateway(new Gateway("SERIAL_NUM3", "NAME_A", "127.0.0.1"));
        gatewayService.deleteGatewayById(gateway.getIdg());
        Optional<Gateway> found = gatewayRepository.findById(gateway.getIdg());
        assertThat(found.isPresent()).isFalse();
    }

    @Test
    void getGatewayById() {
        Gateway gateway = gatewayService.addGateway(new Gateway("SERIAL_NUM3", "NAME_A", "127.0.0.1"));
        Gateway found = gatewayService.getGatewayById(gateway.getIdg());
        assertThat(found.getIdg()).isNotNull();
    }

//    @Test
////    @Disabled
//    void getGatewayDetailsById() {
//        Gateway gateway = gatewayRepository.save(new Gateway("SERIAL_NUM3", "NAME_A", "127.0.0.1"));
//        Peripheral peripheral = peripheralRepository.save(new Peripheral("123456", "vendor_name", LocalDateTime.now(), "online", gateway));
//        Map<String, Object> gatewayMap = gatewayService.getGatewayDetailsById(gateway.getIdg());
//        Map<String, Object> peripheralMap = (Map<String, Object>) gatewayMap.get("peripherals");
//
//        assertThat(gatewayMap.get("id")).isEqualTo(gateway.getIdg());
//        assertThat(peripheralMap.get("id")).isEqualTo(peripheral.getIdp());
//    }
//
//    @Test
//    @Disabled
//    void getAllGatewaysDetails() {
//    }
}