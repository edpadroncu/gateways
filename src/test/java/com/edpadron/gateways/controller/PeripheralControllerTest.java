package com.edpadron.gateways.controller;

import com.edpadron.gateways.common.Helper;
import com.edpadron.gateways.entity.Gateway;
import com.edpadron.gateways.entity.Peripheral;
import com.edpadron.gateways.service.PeripheralService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PeripheralController.class)
class PeripheralControllerTest {

    @MockBean
    PeripheralService peripheralService;

    @Autowired
    MockMvc mockMvc;

    private ObjectMapper mapper = Helper.mapper();
    private Peripheral peripheral = null;
    private List<Peripheral> peripheralList = new ArrayList<>();
    private Gateway gateway = null;

    @BeforeEach
    void setUp() {
        peripheral = new Peripheral(Long.valueOf(1),"123456", "vendor_name", LocalDateTime.now(), "online", null);
        peripheralList.add(peripheral);
        gateway = new Gateway(Long.valueOf(1),"SERIAL_NUM3", "NAME_A", "127.0.0.1", null);
    }

    @Test
    void getAll() throws Exception {
        Mockito.when(peripheralService.getAllPeripheral()).thenReturn(peripheralList);
        MvcResult mvcResult = mockMvc.perform(get("/peripherals"))
                .andExpect(status().isOk())
                .andReturn();

        Map<String, Object> response = Common.commonAsserts(mvcResult);
        List<Map<String, Object>> data = (List<Map<String, Object>>) response.getOrDefault("data", new ArrayList<>());
        Assert.notEmpty(data, "property 'data' must be a list, not empty.");
    }

    @Test
    void add() throws Exception {
        Mockito.when(peripheralService.addPeripheral(peripheral)).thenReturn(peripheral);

        MvcResult mvcResult = mockMvc
                .perform(post("/peripherals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(peripheral)))
                .andExpect(status().isCreated())
                .andReturn();

        Map<String, Object> response = Common.commonAsserts(mvcResult);
        Map<String, Object> data = (Map<String, Object>) response.getOrDefault("data", new HashMap<>());
        Assertions.assertEquals(peripheral.getUid(), data.get("uid"));
    }

    @Test
    void deletePeripheral() throws Exception {
        Mockito.when(peripheralService.deletePeripheralById(Long.valueOf(1))).thenReturn("Deleted");

        MvcResult mvcResult = mockMvc
                .perform(delete("/peripherals/1"))
                .andExpect(status().isOk())
                .andReturn();

        Map<String, Object> response = Common.commonAsserts(mvcResult);
        Assertions.assertEquals(response.get("data").toString(), "Deleted");
    }

    @Test
    void getById() throws Exception {

        Mockito.when(peripheralService.getPeripheralById(peripheral.getIdp())).thenReturn(peripheral);

        MvcResult mvcResult = mockMvc.perform(get("/peripherals/1"))
                .andExpect(status().isOk())
                .andReturn();

        Map<String, Object> response = Common.commonAsserts(mvcResult);
        Map<String, Object> data = (Map<String, Object>) response.getOrDefault("data", new HashMap<>());
        Assertions.assertEquals(data.get("idp").toString(), peripheral.getIdp().toString());
    }

    @Test
    void addPerToGtw() throws Exception {
        Map<String, Object> perMap = mapper.convertValue(peripheral, Map.class);
        Map<String, Object> gateMap = mapper.convertValue(gateway, Map.class);
        perMap.put("gateway", gateMap);
        Mockito.when(peripheralService.addPerToGtw(peripheral.getIdp(), gateway.getIdg())).thenReturn(perMap);

        MvcResult mvcResult = mockMvc
                .perform(post("/peripherals/1/gateways/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        Map<String, Object> response = Common.commonAsserts(mvcResult);
        Map<String, Object> data = (Map<String, Object>) response.getOrDefault("data", new HashMap<>());
        Map<String, Object> gatewayMap = (Map<String, Object>) data.getOrDefault("gateway", new HashMap<>());

        Assertions.assertEquals(peripheral.getIdp().toString(), data.get("idp").toString());
        Assertions.assertEquals(gateway.getIdg().toString(), gatewayMap.get("idg").toString());
    }

    @Test
    void deleteFromGtw() throws Exception {
        Map<String, Object> perMap = mapper.convertValue(peripheral, Map.class);
        Mockito.when(peripheralService.removePerFromGtw(peripheral.getIdp())).thenReturn(perMap);

        MvcResult mvcResult = mockMvc
                .perform(delete("/peripherals/1/gateways")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        Map<String, Object> response = Common.commonAsserts(mvcResult);
        Map<String, Object> data = (Map<String, Object>) response.getOrDefault("data", new HashMap<>());
        Assertions.assertEquals(peripheral.getIdp().toString(), data.get("idp").toString());
    }
}