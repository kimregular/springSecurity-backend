package org.example.springsecuritybackend.domain.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {

	private String id;
	private String username;
	private String password;
	private int age;
	private String roles;
}
