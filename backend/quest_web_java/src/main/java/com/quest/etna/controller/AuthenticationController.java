package com.quest.etna.controller;

import java.util.Optional;

import com.quest.etna.config.AuthenticationService;
import com.quest.etna.config.JwtUserDetailsService;
import com.quest.etna.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.quest.etna.repositories.UserRepository;

@RestController
public class AuthenticationController {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private AuthenticationService service;

	private final PasswordEncoder passwordEncoder;
	private final JwtUserDetailsService userDetailsService;


	public AuthenticationController(PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUserDetailsService userDetailsService) {
		this.passwordEncoder = passwordEncoder;
		this.userDetailsService = userDetailsService;
	}

	@PostMapping(
			path="/register",
			consumes = MediaType.APPLICATION_JSON_VALUE, 
	        produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<Object> register(@RequestBody User newUser) {
		UserDTO userDTO = new UserDTO("");
		if (newUser.getPassword() == null || newUser.getUsername() == null) {
			userDTO.setErrorMessage();
			return new ResponseEntity<>(userDTO, HttpStatus.BAD_REQUEST);
		}
		if (newUser.getPassword().isEmpty() || newUser.getUsername().isEmpty()) {
			userDTO.setErrorMessage();
			return new ResponseEntity<>(userDTO, HttpStatus.BAD_REQUEST);
		}
		
		Optional<User> user = userRepository.findByUsername(newUser.getUsername());
		
		if (user.isEmpty()) {
			String lowercaseUsername = newUser.getUsername().toLowerCase();
			newUser.setUsername(lowercaseUsername);
			if (newUser.getUsername().equals("admin")) {
				newUser.setRole(UserRole.ROLE_ADMIN);
			} else {
				newUser.setRole(UserRole.ROLE_USER);
			}
			newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
			return new ResponseEntity<>(service.register(newUser), HttpStatus.resolve(201));
		}
		userDTO.setDuplicataMessage();
		return new ResponseEntity<>(userDTO, HttpStatus.CONFLICT);
	}

	@PostMapping("/authenticate")
	public ResponseEntity<Object> authenticate(@RequestBody AuthenticationRequest newUser)  {
		UserDTO userDTO = new UserDTO("");
		if (newUser.getPassword() == null || newUser.getUsername() == null) {
			userDTO.setErrorMessage();
			return new ResponseEntity<>(userDTO, HttpStatus.BAD_REQUEST);
		}
		if (newUser.getPassword().isEmpty() || newUser.getUsername().isEmpty()) {
			userDTO.setErrorMessage();
			return new ResponseEntity<>(userDTO, HttpStatus.BAD_REQUEST);
		}

		String lowercaseUsername = newUser.getUsername().toLowerCase();
		Optional<User> user = userRepository.findByUsername(lowercaseUsername);

		if (user.isPresent()) {
			try	{
				newUser.setUsername(lowercaseUsername);
				return ResponseEntity.ok(service.authenticate(newUser));
			}
			catch (Exception e){
				userDTO.setMessage(e.getMessage());
				return new ResponseEntity<>(userDTO, HttpStatus.resolve(401));
			}

		}

		userDTO.setMessage("Bad credentials");
		return new ResponseEntity<>(userDTO, HttpStatus.resolve(401));
	}

	@GetMapping("/me")
	public ResponseEntity<Object> me() {
		var userDetails = this.userDetailsService.loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		return ResponseEntity.ok(userDetails);
	}
}