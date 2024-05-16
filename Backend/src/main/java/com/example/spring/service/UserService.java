package com.example.spring.service;

import com.example.spring.entity.Users;

import java.util.List;

public interface UserService {
	Users findByUserName(String userName);
	boolean existsByUserName(String userName);
	boolean existsByEmail(String email);
	Users saveOrUpdate(Users user);
	String getHomeIdByUserName(String userName);
	List<String> getEmailByHomeId(String homeId);
	String getIdCardByUserName(String userName);
}
