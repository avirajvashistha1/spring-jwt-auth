package io.aviraj.spring.model;

public class AuthenticationResponse {

	private final String jwtToken;

	/**
	 * @return the jwtToken
	 */
	public String getJwtToken() {
		return jwtToken;
	}

	/**
	 * @param jwtToken
	 */
	public AuthenticationResponse(String jwtToken) {
		this.jwtToken = jwtToken;
	}
}
