package com.example.spring.payload.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {
	private String token;
	private String type = "Bearer";
	private String userName;
	private String email;
	private String phone;
	private String deviceId;
	private List<String>  listRoles;

	public JwtResponse(String token, String userName, String email, String phone, List<String> listRoles,String deviceId) {
		this.token = token;
		this.userName = userName;
		this.email = email;
		this.phone = phone;
		this.listRoles = listRoles;
		this.deviceId = deviceId;
	}
}
