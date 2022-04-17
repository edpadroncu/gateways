package com.edpadron.gateways.controller;

import com.edpadron.gateways.common.Helper;
import com.edpadron.gateways.entity.Peripheral;
import com.edpadron.gateways.service.PeripheralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController()
public class PeripheralController {

    @Autowired
    private PeripheralService peripheralService;

    @Autowired
    private Helper helper;

    @GetMapping("/peripherals")
    public ResponseEntity<?> getAll(){
        return helper.httpResponse(true, peripheralService.getAllPeripheral(), HttpStatus.OK);
    }

    @PostMapping("/peripherals")
    public ResponseEntity<?> add(@Valid @RequestBody Peripheral peripheral) {
        return helper.httpResponse(true, peripheralService.addPeripheral(peripheral), HttpStatus.CREATED);
    }

    @DeleteMapping("/peripherals/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return helper.httpResponse(true, peripheralService.deletePeripheralById(id), HttpStatus.OK);
    }

    @GetMapping("/peripherals/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return helper.httpResponse(true, peripheralService.getPeripheralById(id), HttpStatus.OK);
    }

    @PostMapping("/peripherals/{idPer}/gateways/{idGtw}")
    public ResponseEntity<?> addPerToGtw(@PathVariable Long idPer, @PathVariable Long idGtw) {
        return helper.httpResponse(true, peripheralService.addPerToGtw(idPer, idGtw), HttpStatus.OK);
    }

    @DeleteMapping("/peripherals/{idPer}/gateways")
    public ResponseEntity<?> deleteFromGtw(@PathVariable Long idPer) {
        return helper.httpResponse(true, peripheralService.removePerFromGtw(idPer), HttpStatus.OK);
    }
}
