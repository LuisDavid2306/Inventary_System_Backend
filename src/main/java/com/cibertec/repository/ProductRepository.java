package com.cibertec.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cibertec.dto.ProductProjection;
import com.cibertec.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
	List<Product> findByActiveTrue();
	Optional<Product> findByIdAndActiveTrue(Long id);
	
	@Query("""
		    SELECT p
		    FROM Product p
		    WHERE p.active = true
		    AND (
		        CAST(p.id AS string) LIKE %:keyword%
		        OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
		        OR LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
		    )
		""")
		List<Product> search(@Param("keyword") String keyword);
}
