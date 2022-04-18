package com.edpadron.gateways.service;

import com.edpadron.gateways.entity.Gateway;
import com.edpadron.gateways.entity.Peripheral;
import com.edpadron.gateways.exceptions.CustomException;
import com.edpadron.gateways.exceptions.Ipv4Exception;
import com.edpadron.gateways.exceptions.StatusException;
import com.edpadron.gateways.exceptions.UidException;
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
class PeripheralServiceTest {

    @Autowired
    private GatewayRepository gatewayRepository;

    @Autowired
    private PeripheralRepository peripheralRepository;

    private PeripheralService peripheralService;

    private GatewayService gatewayService;

    @BeforeEach
    void setUp() {
        gatewayService = new GatewayService(gatewayRepository);
        peripheralService = new PeripheralService(peripheralRepository, gatewayService);
    }

    @Test
    void addPeripheral() {
        Peripheral peripheral = peripheralService.addPeripheral(new Peripheral("123456", "vendor_name", LocalDateTime.now(), "online"));
        assertThat(peripheral.getIdp()).isNotNull();
    }

    @Test
    void addPeripheral_StatusException() {
        assertThatThrownBy(() -> peripheralService.addPeripheral(new Peripheral("123456", "vendor_name", LocalDateTime.now(), "on")))
                .isInstanceOf(StatusException.class);
    }

    @Test
    void addPeripheral_UidException() {
        assertThatThrownBy(() -> peripheralService.addPeripheral(new Peripheral("123456AAA", "vendor_name", LocalDateTime.now(), "online")))
                .isInstanceOf(UidException.class);
    }

    @Test
    void getAllPeripheral() {
        peripheralService.addPeripheral(new Peripheral("123456", "vendor_name", LocalDateTime.now(), "online"));
        peripheralService.addPeripheral(new Peripheral("123456", "vendor_name", LocalDateTime.now(), "online"));
        assertThat(peripheralService.getAllPeripheral().size()).isEqualTo(2);
    }

    @Test
    void deletePeripheralById() {
        Peripheral peripheral = peripheralService.addPeripheral(new Peripheral("123456", "vendor_name", LocalDateTime.now(), "online"));
        peripheralService.deletePeripheralById(peripheral.getIdp());
        Optional<Peripheral> found = peripheralRepository.findById(peripheral.getIdp());
        assertThat(found.isPresent()).isFalse();
    }

    @Test
    void deletePeripheralById_ValidationException() {
        assertThatThrownBy(() -> peripheralService.deletePeripheralById(Long.valueOf(1)))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Peripheral id not found: 1");
    }

    @Test
    void getPeripheralById() {
        Peripheral peripheral = peripheralService.addPeripheral(new Peripheral("123456", "vendor_name", LocalDateTime.now(), "online"));
        Peripheral found = peripheralService.getPeripheralById(peripheral.getIdp());
        assertThat(found).isNotNull();
    }

    @Test
    void getPeripheralById_ValidationException() {
        assertThatThrownBy(() -> peripheralService.getPeripheralById(Long.valueOf(1)))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Peripheral id not found: 1");
    }

    @Test
    void addPerToGtw() {
        Gateway gateway = gatewayRepository.save(new Gateway("SERIAL_NUM3", "NAME_A", "127.0.0.1"));
        Peripheral peripheral = peripheralRepository.save(new Peripheral("123456", "vendor_name", LocalDateTime.now(), "online"));

        Map<String, Object> peripheralMap = peripheralService.addPerToGtw(peripheral.getIdp(), gateway.getIdg());
        Gateway gatewayFromMap = (Gateway) peripheralMap.get("gateway");

        assertThat(peripheralMap.get("idp")).isEqualTo(peripheral.getIdp());
        assertThat(gatewayFromMap.getIdg()).isEqualTo(gateway.getIdg());
    }

    @Test
    void addPerToGtw_CustomException() {
        Gateway gateway = gatewayRepository.save(new Gateway("SERIAL_NUM3", "NAME_A", "127.0.0.1"));
        for (int i = 0; i < 10; i++) {
            peripheralRepository.save(new Peripheral("123456", "vendor_name", LocalDateTime.now(), "online", gateway));
        }
        Peripheral peripheral = peripheralRepository.save(new Peripheral("123456", "vendor_name", LocalDateTime.now(), "online"));
        assertThatThrownBy(() -> peripheralService.addPerToGtw(peripheral.getIdp(), gateway.getIdg()))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining("No more than 10 peripheral devices are allowed for a gateway");
    }

    @Test
    void removePerFromGtw() {
        Gateway gateway = gatewayRepository.save(new Gateway("SERIAL_NUM3", "NAME_A", "127.0.0.1"));
        Peripheral peripheral = peripheralRepository.save(new Peripheral("123456", "vendor_name", LocalDateTime.now(), "online", gateway));

        Map<String, Object> peripheralMap = peripheralService.removePerFromGtw(peripheral.getIdp());
        assertThat(peripheralMap.get("idp")).isEqualTo(peripheral.getIdp());
        assertThat(peripheralMap.get("gateway")).isNull();
    }

    @Test
    void peripheralsByGateway() {
        Gateway gateway = gatewayRepository.save(new Gateway("SERIAL_NUM3", "NAME_A", "127.0.0.1"));
        peripheralRepository.save(new Peripheral("123456", "vendor_name", LocalDateTime.now(), "online", gateway));
        peripheralRepository.save(new Peripheral("123456", "vendor_name", LocalDateTime.now(), "online", gateway));
        assertThat(peripheralService.peripheralsByGateway(gateway).size()).isEqualTo(2);
    }

    @Test
    void peripheralToMap() {
        Gateway gateway = gatewayRepository.save(new Gateway("SERIAL_NUM3", "NAME_A", "127.0.0.1"));
        Peripheral peripheral = peripheralRepository.save(new Peripheral("123456", "vendor_name", LocalDateTime.now(), "online", gateway));

        Map<String, Object> peripheralMap = peripheralService.peripheralToMap(peripheral);
        Gateway gatewayFromMap = (Gateway) peripheralMap.get("gateway");

        assertThat(peripheralMap.get("idp")).isEqualTo(peripheral.getIdp());
        assertThat(gatewayFromMap.getIdg()).isEqualTo(gateway.getIdg());
    }
}