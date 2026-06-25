package com.cibertec.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cibertec.dto.ProductProjection;
import com.cibertec.entity.Product;
import com.cibertec.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import util.SecurityUtil;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
	
	private final ProductRepository repository;
	private final AuditService auditService;
	
	
	 @Override
	    public List<Product> findAll() {
	        return repository.findAll();  
	    }

	@Override
	public Product findById(Long id) {
	    return repository.findByIdAndActiveTrue(id)
	            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
	}

    @Override
    public Product save(Product product) {

        product.setActive(true);

        Product saved = repository.save(product);

        auditService.saveLog(
                SecurityUtil.getCurrentUsername(),
                "CREATE",
                saved.getId());

        return saved;
    }

    @Override
    public Product update(Long id, Product product) {

        Product existing = findById(id);

        existing.setName(product.getName());
        existing.setDescription(product.getDescription());
        existing.setPrice(product.getPrice());
        existing.setStock(product.getStock());

        Product updated = repository.save(existing);

        auditService.saveLog(
                SecurityUtil.getCurrentUsername(),
                "UPDATE",
                updated.getId());

        return updated;
    }

    @Override
    public void delete(Long id) {

        Product product = findById(id);

        product.setActive(false);

        repository.save(product);

        auditService.saveLog(
                SecurityUtil.getCurrentUsername(),
                "DEACTIVATE",
                id);
    }
    
    public Product activate(Long id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        product.setActive(true);

        return repository.save(product);
    }
    
    @Override
    public List<Product> search(String keyword) {
        return repository.search(keyword);
    }
}
