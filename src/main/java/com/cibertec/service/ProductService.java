package com.cibertec.service;

import java.util.List;

import com.cibertec.dto.ProductProjection;
import com.cibertec.entity.Product;

public interface ProductService {
	
	List<Product> findAll();
    Product findById(Long id);
    Product save(Product product);
    Product update(Long id, Product product);
    void delete(Long id);
    Product activate(Long id);
    
    List<Product> search(String keyword);

}
