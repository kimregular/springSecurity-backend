package org.example.springsecuritybackend.global.details;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

@Data
public class FormAuthenticationDetails extends WebAuthenticationDetails {

	private String secretKey;

	public FormAuthenticationDetails(HttpServletRequest request) {
		super(request);
		this.secretKey = request.getParameter("secret_key");
	}
}
