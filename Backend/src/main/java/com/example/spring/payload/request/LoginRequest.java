package com.example.spring.payload.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequest {
	private String userName;
	private String passWord;
}
