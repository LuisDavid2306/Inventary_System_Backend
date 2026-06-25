package com.cibertec.service;

import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import com.cibertec.entity.ProductLock;
import com.cibertec.repository.ProductLockRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class LockServiceImpl implements LockService {
	
	private final ProductLockRepository repository;
	
	
    @Override
    public void lockProduct(Long productId, String username) {

    	if (repository.existsByProductId(productId)) {

            ProductLock lock =
                    repository.findByProductId(productId)
                            .orElseThrow();

            throw new RuntimeException(
                    "Producto bloqueado por: "
                    + lock.getLockedBy());
        }

        ProductLock lock = ProductLock.builder()
        		.productId(productId)
                .lockedBy(username)
                .lockedAt(LocalDateTime.now())
                .build();

        repository.save(lock);
    }

    @Override
    public void unlockProduct(Long productId) {

        repository.deleteByProductId(productId);
    }
    
    
    @Override
    public boolean isLocked(Long productId) {

        return repository.existsByProductId(productId);
    }

    @Override
    public String getLockedBy(Long productId) {

        return repository.findByProductId(productId)
        		.map(ProductLock::getLockedBy)
                .orElse(null);
    }
}
