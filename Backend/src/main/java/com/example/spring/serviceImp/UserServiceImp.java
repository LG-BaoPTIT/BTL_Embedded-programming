package com.example.spring.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.spring.entity.Users;
import com.example.spring.repository.UserRepository;
import com.example.spring.service.UserService;

import java.util.List;

@Service
public class UserServiceImp implements UserService {
	@Autowired
	UserRepository userRepository;

	@Override
	public Users findByUserName(String userName) {
		// TODO Auto-generated method stub
		return userRepository.findByUserName(userName);
	}

	@Override
	public boolean existsByUserName(String userName) {
		// TODO Auto-generated method stub
		return userRepository.existsByUserName(userName);
	}

	@Override
	public boolean existsByEmail(String email) {
		// TODO Auto-generated method stub
		return userRepository.existsByEmail(email);
	}

	@Override
	public Users saveOrUpdate(Users user) {
		// TODO Auto-generated method stub
		return userRepository.save(user);
	}

	@Override
	public String getHomeIdByUserName(String userName) {
		return userRepository.getHomeIdByUserName(userName);
	}

	@Override
	public List<String> getEmailByHomeId(String homeId) {
		return userRepository.getEmailByHomeId(homeId);
	}

	@Override
	public String getIdCardByUserName(String userName) {
		return userRepository.getIdCardByUserName(userName);
	}


}
