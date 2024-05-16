package com.example.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.spring.entity.Users;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer>{
	
	Users findByUserName(String userName);
	boolean existsByUserName(String userName);
	boolean existsByEmail(String email);
	@Query(value = "SELECT home_id FROM users WHERE user_name = :userName", nativeQuery = true)
	String getHomeIdByUserName(@Param("userName") String userName);

	@Query(value = "SELECT email FROM users WHERE home_id = :homeId",nativeQuery = true)
	List<String> getEmailByHomeId(@Param("homeId") String homeId);

	@Query(value = "SELECT id_card FROM users WHERE user_name = :userName", nativeQuery = true)
	String getIdCardByUserName(@Param("userName") String userName);
}
