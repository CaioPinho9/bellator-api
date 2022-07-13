package com.github.CaioPinho9.bellatorapi.repository;

import com.github.CaioPinho9.bellatorapi.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

}
