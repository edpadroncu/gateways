package com.edpadron.gateways.service;

import com.edpadron.gateways.entity.Gateway;
import com.edpadron.gateways.entity.Peripheral;
import com.edpadron.gateways.exceptions.Ipv4Exception;
import com.edpadron.gateways.repository.GatewayRepository;
import com.edpadron.gateways.repository.PeripheralRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class GatewayServiceTest {

    @Autowired
    private GatewayRepository gatewayRepository;

    @Autowired
    private PeripheralRepository peripheralRepository;

    private PeripheralService peripheralService;

    private GatewayService gatewayService;

    @BeforeEach
    void setUp() {
        peripheralService = new PeripheralService(peripheralRepository);
        gatewayService = new GatewayService(gatewayRepository, peripheralService);
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
    void addGateway_Ipv4Exception(){
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
    void deleteGatewayById_ValidationException() {
        assertThatThrownBy(() -> gatewayService.deleteGatewayById(Long.valueOf(1)))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Gateway id not found: 1");
    }

    @Test
    void getGatewayById() {
        Gateway gateway = gatewayService.addGateway(new Gateway("SERIAL_NUM3", "NAME_A", "127.0.0.1"));
        Gateway found = gatewayService.getGatewayById(gateway.getIdg());
        assertThat(found.getIdg()).isNotNull();
    }

    @Test
    void getGatewayById_ValidationException() {
        assertThatThrownBy(() -> gatewayService.getGatewayById(Long.valueOf(1)))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Gateway id not found: 1");
    }

    @Test
    void getGatewayDetailsById() {
        Gateway gateway = gatewayRepository.save(new Gateway("SERIAL_NUM3", "NAME_A", "127.0.0.1"));
        Peripheral peripheral = peripheralRepository.save(new Peripheral("123456", "vendor_name", LocalDateTime.now(), "online", gateway));

        Map<String, Object> gatewayMap = gatewayService.getGatewayDetailsById(gateway.getIdg());
        List<Peripheral> peripheralList = (List<Peripheral>) gatewayMap.get("peripherals");

        assertThat(gatewayMap.get("idg")).isEqualTo(gateway.getIdg());
        assertThat(peripheralList.size()).isGreaterThan(0);
        assertThat(peripheralList.get(0).getIdp()).isEqualTo(peripheral.getIdp());
    }

    @Test
    void getAllGatewaysDetails() {
        Gateway gateway = gatewayRepository.save(new Gateway("SERIAL_NUM3", "NAME_A", "127.0.0.1"));
        Peripheral peripheral = peripheralRepository.save(new Peripheral("123456", "vendor_name", LocalDateTime.now(), "online", gateway));

        List<Map<String, Object>> gatewayList = gatewayService.getAllGatewaysDetails();
        List<Peripheral> peripheralList = (List<Peripheral>) gatewayList.get(0).get("peripherals");

        assertThat(gatewayList.size()).isGreaterThan(0);
        assertThat(peripheralList.size()).isGreaterThan(0);
        assertThat(gatewayList.get(0).get("idg")).isEqualTo(gateway.getIdg());
        assertThat(peripheralList.get(0).getIdp()).isEqualTo(peripheral.getIdp());
    }
}