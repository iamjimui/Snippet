package com.quest.etna.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.quest.etna.model.User;
import com.quest.etna.repositories.UserRepository;

@RestController
public class DefaultController {
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping(value="/{username}")
	public ResponseEntity<User> findByUsername(@PathVariable("username") String username) {
		Optional<User> user = userRepository.findByUsername(username);
		if (user.isEmpty()) {
			return new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<User>(userRepository.findByUsername(username).get(), HttpStatus.OK);
	}
	
    @GetMapping("/testSuccess")
    public String testSuccess() {
        return "success";
    }

    @GetMapping("/testNotFound")
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String testNotFound() {
        return "not found";
    }

    @GetMapping("/testError")
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String testError(){
        return "error";
    }
}