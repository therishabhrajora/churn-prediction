package com.churn.controller;

import com.churn.service.PredictionService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/predictions")
public class PredictionController {
    private final PredictionService service;
    public PredictionController(PredictionService service) { this.service = service; }

    @PostMapping("/{customerId}")
    public Object predict(@PathVariable Long customerId) { return service.request(customerId); }

    @GetMapping("/{id}")
    public Object get(@PathVariable Long id) { return service.get(id); }

    @GetMapping("/customer/{customerId}")
    public Object history(@PathVariable Long customerId) { return service.history(customerId); }

    @GetMapping("/customer/{customerId}/latest")
    public Object latest(@PathVariable Long customerId) { return service.latest(customerId); }
}
