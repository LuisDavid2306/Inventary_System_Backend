package com.cibertec.service;

import java.util.List;

import com.cibertec.dto.LoginRequest;
import com.cibertec.dto.RegisterRequest;
import com.cibertec.entity.Product;
import com.cibertec.entity.User;

public interface AuthService {

	List<User> findAll();
	void register(RegisterRequest request);
    String login(LoginRequest request);
    
}
