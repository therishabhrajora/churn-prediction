package com.churn.controller;
import com.churn.dto.CustomerRequest; import com.churn.service.CustomerService; import org.springframework.web.bind.annotation.*; import jakarta.validation.Valid;
@RestController @RequestMapping("/api/customers") public class CustomerController { private final CustomerService s; public CustomerController(CustomerService s){this.s=s;} @PostMapping public Object create(@Valid @RequestBody CustomerRequest r){return s.create(r);} @GetMapping public Object all(){return s.all();} @GetMapping("/{id}") public Object get(@PathVariable Long id){return s.get(id);} }
