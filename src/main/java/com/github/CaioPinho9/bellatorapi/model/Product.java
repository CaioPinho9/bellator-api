package com.github.CaioPinho9.bellatorapi.model;

import com.github.CaioPinho9.bellatorapi.dto.ProductDTO;

import javax.persistence.*;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;
    private double price;
    private double size;

    public Product(String name, double price, double size) {
        this.name = name;
        this.price = price;
        this.size = size;
    }

    public Product() {
    }

    public static Product convert(ProductDTO productDTO) {
        return new Product(productDTO.getName(), productDTO.getPrice(), productDTO.getSize());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }
}
