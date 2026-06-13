package com.cibertec.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cibertec.entity.Product;
import com.cibertec.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import util.SecurityUtil;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
	
	private final ProductRepository repository;
	private final AuditService auditService;
	private final LockService lockService;
	
    @Override
    public List<Product> findAll() {
        return repository.findAll();
    }

    @Override
    public Product findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    @Override
    public Product save(Product product) {
    	
    	Product saved = repository.save(product);
    	auditService.saveLog(
    	        SecurityUtil.getCurrentUsername(),
    	        "CREATE",
    	        saved.getId());

    	return saved;
    }

    @Override
    public Product update(Long id, Product product) {

    	if (lockService.isLocked(id)) {

            String lockedBy = lockService.getLockedBy(id);

            if (!lockedBy.equals(SecurityUtil.getCurrentUsername())) {
                throw new RuntimeException(
                        "Producto bloqueado por: " + lockedBy);
            }
        }
    	
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
    	
    	if (lockService.isLocked(id)) {

            String lockedBy = lockService.getLockedBy(id);

            if (!lockedBy.equals(SecurityUtil.getCurrentUsername())) {
                throw new RuntimeException("Producto bloqueado por: " + lockedBy);
            }
        }
    	
    	auditService.saveLog(
    	        SecurityUtil.getCurrentUsername(),
    	        "DELETE",
    	        id);

    	repository.deleteById(id);
    }
}
