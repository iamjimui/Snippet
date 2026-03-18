package com.quest.etna.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.quest.etna.config.JwtTokenUtil;
import com.quest.etna.model.JwtUserDetails;
import com.quest.etna.model.User;
import com.quest.etna.model.UserDTO;
import com.quest.etna.model.UserRole;
import com.quest.etna.model.UserSuccessMessage;
import com.quest.etna.repositories.UserRepository;

@RestController
public class UserController {
	@Autowired
	private UserRepository userRepository;

	private final PasswordEncoder passwordEncoder;
	
	private final JwtTokenUtil jwtService = new JwtTokenUtil();

	public UserController(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	@GetMapping(value="/user")
	public @ResponseBody ResponseEntity<?> getAll() {
		Iterable<User> users = userRepository.findAll();
		List<User> userList = new ArrayList<User>();
		users.forEach(userList::add);
		return new ResponseEntity<>(userList, HttpStatus.OK);
	}
	
	@GetMapping(value="/user/{id}")
	public ResponseEntity<?> getById(@PathVariable("id") int id) {
		Optional<User> getUser = userRepository.findById(id);
		
        if (getUser.isEmpty()) {
        	UserDTO userDTO = new UserDTO("");
			userDTO.setNotFoundMessage();
			return new ResponseEntity<>(userDTO, HttpStatus.NOT_FOUND);
        }
		return new ResponseEntity<>(getUser.get(), HttpStatus.OK);
	}
	
	@PutMapping(value="/user/{id}")
	public ResponseEntity<?> update(@PathVariable("id") int id, @RequestBody User user) {
		Optional<User> existUser = userRepository.findById(id);
		if (existUser.isEmpty()) {
			UserDTO userDTO = new UserDTO("");
			userDTO.setNotFoundMessage();
			return new ResponseEntity<>(userDTO, HttpStatus.NOT_FOUND);
		}
		
		JwtUserDetails jwtUserDetails = (JwtUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
        if (user.getUsername() != null && !user.getUsername().isEmpty()) {
        	String lowercaseUsername = user.getUsername().toLowerCase();
        	existUser.get().setUsername(lowercaseUsername);
        }
        if (user.getRole() != null && jwtUserDetails.getRole() == UserRole.ROLE_ADMIN) {
        	existUser.get().setRole(user.getRole());
        }

		if (user.getPassword() != null && jwtUserDetails.getRole() == UserRole.ROLE_ADMIN && jwtUserDetails.getId() == id) {
			existUser.get().setPassword(passwordEncoder.encode(user.getPassword()));
		}

		if (jwtUserDetails.getId() == id || jwtUserDetails.getRole() == UserRole.ROLE_ADMIN) {
			userRepository.save(existUser.get());
	        Optional<User> updatedUser = userRepository.findById(id);
	        return new ResponseEntity<User>(updatedUser.get(), HttpStatus.OK);
		}
		
		UserDTO userDTO = new UserDTO("");
		userDTO.setForbiddenMessage();
        return new ResponseEntity<>(userDTO, HttpStatus.UNAUTHORIZED);
	}
	
	@DeleteMapping(value="/user/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") int id) {
		Optional<User> user = userRepository.findById(id);
		if (user.isEmpty()) {
			UserSuccessMessage result = new UserSuccessMessage(false);
			return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
		}
		
		JwtUserDetails jwtUserDetails = (JwtUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        if (jwtUserDetails.getId() == id || jwtUserDetails.getRole() == UserRole.ROLE_ADMIN) {
        	userRepository.deleteById(user.get().getId());
    		UserSuccessMessage result = new UserSuccessMessage(true);
    		return new ResponseEntity<>(result, HttpStatus.OK);
		}
        
        UserDTO userDTO = new UserDTO("");
		userDTO.setForbiddenMessage();
        return new ResponseEntity<>(userDTO, HttpStatus.FORBIDDEN);
	}
}
