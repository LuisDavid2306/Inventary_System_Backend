package com.cibertec.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.cibertec.service.LockService;

import lombok.RequiredArgsConstructor;
import util.SecurityUtil;

@RestController
@RequestMapping("/api/locks")
@RequiredArgsConstructor
public class LockController {
	
	private final LockService lockService;


	@PostMapping("/{productId}")
	public ResponseEntity<?> lockProduct(
	        @PathVariable Long productId) {

	    try {

	        lockService.lockProduct(
	                productId,
	                SecurityUtil.getCurrentUsername());

	        return ResponseEntity.ok(
	                Map.of(
	                        "success", true,
	                        "message", "Producto bloqueado"));

	    } catch (RuntimeException ex) {

	        return ResponseEntity.status(HttpStatus.CONFLICT)
	                .body(
	                        Map.of(
	                                "success", false,
	                                "message", ex.getMessage()));
	    }
	}

    @DeleteMapping("/{productId}")
    public String unlockProduct(
            @PathVariable Long productId) {
    	
    	System.out.println("ENTRO AL DELETE");
    	
        lockService.unlockProduct(productId);
        return "Producto desbloqueado";
    }
}
