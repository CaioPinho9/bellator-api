package com.github.CaioPinho9.bellatorapi.service;

import com.github.CaioPinho9.bellatorapi.dto.ProductDTO;
import com.github.CaioPinho9.bellatorapi.model.Product;
import com.github.CaioPinho9.bellatorapi.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    public Product createProduct(ProductDTO productDTO) {
        Product product = new Product(productDTO.getName(), productDTO.getPrice(), productDTO.getSize());
        return productRepository.save(product);
    }

    public Optional<?> update(long id, ProductDTO productDTO) {
        productRepository.findById(id)
                .map(record -> {
                    record.setName(productDTO.getName());
                    record.setPrice(productDTO.getPrice());
                    record.setSize(productDTO.getSize());
                    Product productUpdated = productRepository.save(record);
                    return ResponseEntity.ok().body(productUpdated);
                }).orElse(ResponseEntity.notFound().build());
        return null;
    }

    public Optional<?> delete(long id) {
        return productRepository.findById(id)
                .map(record -> {
                    productRepository.deleteById(id);
                    return ResponseEntity.ok().build();
                });
    }
}
