package com.api.gestion.repositories;


import com.api.gestion.dto.ProductDTO;
import com.api.gestion.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {

    List<ProductDTO> getAllProducts();
}
