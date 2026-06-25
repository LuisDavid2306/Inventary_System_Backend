package com.cibertec.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cibertec.dto.ProductProjection;
import com.cibertec.entity.Product;
import com.cibertec.service.ProductService;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
	
	private final ProductService service;
	
	@GetMapping
    public List<Product> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Product findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public Product save(@RequestBody Product product) {
        return service.save(product);
    }

    @PutMapping("/{id}")
    public Product update(
            @PathVariable Long id,
            @RequestBody Product product) {

        return service.update(id, product);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
    
    @PatchMapping("/{id}/activate")
    public Product activate(@PathVariable Long id) {
        return service.activate(id);
    }
    
    
    @GetMapping("/search")
    public ResponseEntity<List<Product>> search(
            @RequestParam String keyword) {

        return ResponseEntity.ok(service.search(keyword));
    }

}
