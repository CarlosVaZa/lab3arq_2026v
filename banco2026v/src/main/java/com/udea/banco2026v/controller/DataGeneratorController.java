package com.udea.banco2026v.controller;

import com.udea.banco2026v.dto.CustomerDTO;
import com.udea.banco2026v.service.DataGeneratorService;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/test")
@Profile("test")
public class DataGeneratorController {

    private final DataGeneratorService dataGeneratorService;

    public DataGeneratorController(DataGeneratorService dataGeneratorService) {
        this.dataGeneratorService = dataGeneratorService;
    }

    @GetMapping("/generate")
    public ResponseEntity<List<CustomerDTO>> generateData(@RequestParam(defaultValue = "10") int count) {
        List<CustomerDTO> generated = dataGeneratorService.generateCustomers(count);
        return ResponseEntity.ok(generated);
    }
}
