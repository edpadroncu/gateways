package com.edpadron.gateways.controller;

import com.edpadron.gateways.common.Helper;
import com.edpadron.gateways.entity.Gateway;
import com.edpadron.gateways.entity.Peripheral;
import com.edpadron.gateways.service.GatewayService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
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

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(GatewayController.class)
class GatewayControllerTest {

    @MockBean
    GatewayService gatewayService;

    @Autowired
    MockMvc mockMvc;

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    void getAll() throws Exception {
        Gateway gateway = new Gateway("SERIAL_NUM3", "NAME_A", "127.0.0.1");
        List<Gateway> gatewayList = Arrays.asList(gateway);
        Mockito.when(gatewayService.getAllGateways()).thenReturn(gatewayList);

        MvcResult mvcResult = mockMvc.perform(get("/gateways"))
                .andExpect(status().isOk())
                .andReturn();

        Map<String, Object> response = Common.commonAsserts(mvcResult);
        List<Map<String, Object>> data = (List<Map<String, Object>>) response.getOrDefault("data", new ArrayList<>());
        Assert.notEmpty(data, "property 'data' must be a list, not empty.");
    }

    @Test
    void add() throws Exception {
        Gateway gateway = new Gateway("SERIAL_NUM3", "NAME_A", "127.0.0.1");
        Mockito.when(gatewayService.addGateway(gateway)).thenReturn(gateway);

        MvcResult mvcResult = mockMvc
                .perform(post("/gateways")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(gateway)))
                .andExpect(status().isCreated())
                .andReturn();

        Map<String, Object> response = Common.commonAsserts(mvcResult);
        Map<String, Object> data = (Map<String, Object>) response.getOrDefault("data", new HashMap<>());
        Assertions.assertEquals(gateway.getSerial_number(), data.get("serial_number"));
    }

    @Test
    void deleteGateway() throws Exception {
        Mockito.when(gatewayService.deleteGatewayById(Long.valueOf(1))).thenReturn("Deleted");

        MvcResult mvcResult = mockMvc
                .perform(delete("/gateways/1"))
                .andExpect(status().isOk())
                .andReturn();

        Map<String, Object> response = Common.commonAsserts(mvcResult);
        Assertions.assertEquals(response.get("data").toString(), "Deleted");
    }

    @Test
    void getById() throws Exception {
        Map<String, Object> tdata = createGatewayDataTest();
        Mockito.when(gatewayService.getGatewayDetailsById(((Gateway) tdata.get("gateway")).getIdg()))
                .thenReturn(((Map<String, Object>) tdata.get("map")));

        MvcResult mvcResult = mockMvc.perform(get("/gateways/1"))
                .andExpect(status().isOk())
                .andReturn();

        Map<String, Object> response = Common.commonAsserts(mvcResult);
        Map<String, Object> data = (Map<String, Object>) response.getOrDefault("data", new HashMap<>());
        Assertions.assertEquals(((Gateway) tdata.get("gateway")).getIdg(), Long.valueOf(data.get("idg").toString()));
        List<Object> perlist = (List<Object>) data.get("peripherals");
        Assertions.assertEquals(perlist.size(), 1);

    }

    @Test
    void getAllGatewaysDetails() throws Exception {
        Map<String, Object> tdata = createGatewayDataTest();
        List<Map<String, Object>> gatewayListMap = new ArrayList<>();
        gatewayListMap.add(((Map<String, Object>) tdata.get("map")));

        Mockito.when(gatewayService.getAllGatewaysDetails()).thenReturn(gatewayListMap);
        MvcResult mvcResult = mockMvc.perform(get("/gateways/details")).andExpect(status().isOk()).andReturn();

        Map<String, Object> response = Common.commonAsserts(mvcResult);
        List<Object> data = (List<Object>) response.getOrDefault("data", new ArrayList<>());
        Assertions.assertEquals(data.size(), 1);
        Assertions.assertEquals(((Map<String, Object>)data.get(0)).get("idg").toString(), ((Gateway) tdata.get("gateway")).getIdg().toString());
        List<Map<String, Object>> perLis = ((List<Map<String, Object>>)((Map<String, Object>)data.get(0)).get("peripherals"));
        Assertions.assertEquals(perLis.size(), 1);
    }

    /**
     * Creating a Gateway object and a HashMap from the Gateway object.
     * @return HashMap {"gateway": {}, "map": {}}
     */
    private Map<String, Object> createGatewayDataTest(){
        Peripheral peripheral = new Peripheral("123456", "vendor_name", LocalDateTime.now(), "online");
        Set<Peripheral> mapList = new HashSet<>();
        mapList.add(peripheral);
        Gateway gateway = new Gateway(Long.valueOf(1),"SERIAL_NUM3", "NAME_A", "127.0.0.1", mapList);
        Map<String, Object> gMap = Helper.mapper().convertValue(gateway, Map.class);
        gMap.put("peripherals", mapList);

        return new HashMap(){{
            put("gateway", gateway);
            put("map", gMap);
        }};
    }
}