package io.aviraj.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.aviraj.spring.configuration.MyUserDetailsService;
import io.aviraj.spring.model.AuthenticationRequest;
import io.aviraj.spring.model.AuthenticationResponse;
import io.aviraj.spring.utils.JwtUtil;

@RestController
public class SecurityController {
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private MyUserDetailsService userDetailsService;
	
	@Autowired
	AuthenticationManager authenticationManager;

	@RequestMapping("/")
	public String getSecurityHome() {
		return "This is JWT security home page.";
	}
	
	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception{
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUserName(), authenticationRequest.getPassword()));
		} catch(BadCredentialsException ez) {
			throw new Exception("Incorrect Username and Password provided.",ez);
		}
		
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUserName());
		
		String token = jwtUtil.generateToken(userDetails);
		
		return ResponseEntity.ok(new AuthenticationResponse(token));
	}
	
	
	
}
