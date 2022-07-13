package com.github.CaioPinho9.bellatorapi.controller;

import com.github.CaioPinho9.bellatorapi.dto.ProductDTO;
import com.github.CaioPinho9.bellatorapi.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping("/findAll")
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(productService.findAll());
    }

    @GetMapping(path = {"/{id}"})
    public ResponseEntity<?> findById(@PathVariable long id) {
        return productService.findById(id)
                .map(record -> ResponseEntity.ok().body(record))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/")
    public ResponseEntity<?> createProduct(@RequestBody ProductDTO productDTO) {
        return ResponseEntity.ok(productService.createProduct(productDTO));
    }

    @PutMapping(value ="/{id}")
    public ResponseEntity<?> update(@PathVariable("id") long id, @RequestBody ProductDTO productDTO) {
        return ResponseEntity.ok(productService.update(id, productDTO));
    }

    @DeleteMapping(path = {"/{id}"})
    public ResponseEntity<?> delete(@PathVariable long id) {
        return ResponseEntity.ok(productService.delete(id));
    }

}
