package com.edpadron.gateways.controller;

import com.edpadron.gateways.common.Helper;
import com.edpadron.gateways.entity.Gateway;
import com.edpadron.gateways.service.GatewayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController()
public class GatewayController {

    @Autowired
    private GatewayService gatewayService;

    @Autowired
    private Helper helper;

    @GetMapping("/gateways")
    public ResponseEntity<?> getAll(){
        return helper.httpResponse(true, gatewayService.getAllGateways(), HttpStatus.OK);
    }

    @PostMapping("/gateways")
    public ResponseEntity<?> add(@Valid @RequestBody Gateway gateway) {
        return helper.httpResponse(true, gatewayService.addGateway(gateway), HttpStatus.CREATED);
    }

    @DeleteMapping("/gateways/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return helper.httpResponse(true, gatewayService.deleteGatewayById(id), HttpStatus.OK);
    }

    @GetMapping("/gateways/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return helper.httpResponse(true, gatewayService.getGatewayDetailsById(id), HttpStatus.OK);
    }

    @GetMapping("/gateways/details")
    public ResponseEntity<?> getAllGatewaysDetails(){
        return helper.httpResponse(true, gatewayService.getAllGatewaysDetails(), HttpStatus.OK);
    }


}
