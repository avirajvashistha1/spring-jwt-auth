package io.aviraj.spring.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.aviraj.spring.configuration.MyUserDetailsService;
import io.aviraj.spring.utils.JwtUtil;

@Component
public class JwtRequestFilter extends OncePerRequestFilter{
	
	@Autowired
	private MyUserDetailsService userDetailsService;
	
	@Autowired
	private JwtUtil jwtUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		final String authorizationHeader = request.getHeader("Authorization");
		
		String userName = null;
		String jwtToken = null;
		
		if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			jwtToken = authorizationHeader.substring(7);
			userName = jwtUtil.extractUsername(jwtToken);
		}
		
		if(userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
			if(jwtUtil.validateToken(jwtToken, userDetails)) {
				UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				upToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(upToken);
			}
		}
		
		filterChain.doFilter(request, response);
	}
	
}
