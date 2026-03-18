package com.quest.etna.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.quest.etna.config.JwtTokenUtil;
import com.quest.etna.model.Address;
import com.quest.etna.model.AddressDTO;
import com.quest.etna.model.AddressSuccessMessage;
import com.quest.etna.model.JwtUserDetails;
import com.quest.etna.model.User;
import com.quest.etna.model.UserRole;
import com.quest.etna.repositories.AddressRepository;
import com.quest.etna.repositories.UserRepository;

@RestController
public class AddressController {
	@Autowired
	private AddressRepository addressRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	private final JwtTokenUtil jwtService = new JwtTokenUtil();
	
	@GetMapping(value="/address")
	public @ResponseBody ResponseEntity<?> getAll() {
		JwtUserDetails jwtUserDetails = (JwtUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        if (jwtUserDetails.getRole() == UserRole.ROLE_ADMIN) {
        	Iterable<Address> address = addressRepository.findAll();
    		List<Address> addressList = new ArrayList<Address>();
    		address.forEach(addressList::add);
    		return new ResponseEntity<>(addressList, HttpStatus.OK);
        }
		List<Address> listAddress = addressRepository.findByUserId(jwtUserDetails.getId());
		return new ResponseEntity<>(listAddress, HttpStatus.OK);
	}
	
	@GetMapping(value="/address/{id}")
	public ResponseEntity<?> getById(@PathVariable("id") int id) {
		JwtUserDetails jwtUserDetails = (JwtUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        Optional<User> getUser = userRepository.findById(jwtUserDetails.getId());
		Optional<Address> address = addressRepository.findById(id);
		
		if (address.isEmpty()) {
			AddressDTO addressDTO = new AddressDTO("");
			addressDTO.setNotFoundMessage();
			return new ResponseEntity<>(addressDTO, HttpStatus.NOT_FOUND);
		}
		
		if (getUser.isEmpty()) {
			return new ResponseEntity<Address>(HttpStatus.BAD_REQUEST);
		}
		
		if (getUser.get().getId() == address.get().getUser().getId() || getUser.get().getRole() == UserRole.ROLE_ADMIN) {
			return new ResponseEntity<Address>(address.get(), HttpStatus.OK);
		}
		AddressDTO addressDTO = new AddressDTO("");
		addressDTO.setForbiddenMessage();
		return new ResponseEntity<>(addressDTO, HttpStatus.FORBIDDEN);
	}
	
	@PostMapping(value="/address")
	public ResponseEntity<Object> register(@RequestBody Address newAddress) {
		AddressDTO addressDTO = new AddressDTO("");
		if (newAddress.getStreet() == null ||
				newAddress.getPostalCode() == null ||
				newAddress.getCity() == null ||
				newAddress.getCountry() == null) {
			addressDTO.setErrorMessage();
			return new ResponseEntity<>(addressDTO, HttpStatus.BAD_REQUEST);
		}
		if (newAddress.getStreet().isEmpty() ||
				newAddress.getPostalCode().isEmpty() ||
				newAddress.getCity().isEmpty() ||
				newAddress.getCountry().isEmpty()) {
			addressDTO.setErrorMessage();
			return new ResponseEntity<>(addressDTO, HttpStatus.BAD_REQUEST);
		}
		JwtUserDetails jwtUserDetails = (JwtUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        User getUser = userRepository.findByUsername(jwtUserDetails.getUsername()).get();
        newAddress.setUser(getUser);

		addressRepository.save(newAddress);
		return new ResponseEntity<>(newAddress, HttpStatus.CREATED);
	}
	
	@PutMapping(value="/address/{id}")
	public ResponseEntity<?> update(@PathVariable("id") int id, @RequestBody Address address) {
		Optional<Address> existAddress = addressRepository.findById(id);
		
		if (existAddress.isEmpty()) {
			AddressDTO addressDTO = new AddressDTO("");
			addressDTO.setNotFoundMessage();
			return new ResponseEntity<>(addressDTO, HttpStatus.NOT_FOUND);
		}
		
        if (address.getStreet() != null && !address.getStreet().isEmpty()) {
        	existAddress.get().setStreet(address.getStreet());
        }
        if (address.getCity() != null && !address.getCity().isEmpty()) {
        	existAddress.get().setCity(address.getCity());
        }
        if (address.getPostalCode() != null && !address.getPostalCode().isEmpty()) {
        	existAddress.get().setPostalCode(address.getPostalCode());
        }
        if (address.getCountry() != null && !address.getCountry().isEmpty()) {
        	existAddress.get().setCountry(address.getCountry());
        }
        
        JwtUserDetails jwtUserDetails = (JwtUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		if (existAddress.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		if (jwtUserDetails.getId() == existAddress.get().getUser().getId() || jwtUserDetails.getRole() == UserRole.ROLE_ADMIN) {
			addressRepository.save(existAddress.get());
	        Address updatedAddress = addressRepository.findById(id).get();
	        return new ResponseEntity<Address>(updatedAddress, HttpStatus.OK);
		}
		
		AddressDTO addressDTO = new AddressDTO("");
		addressDTO.setForbiddenMessage();
        return new ResponseEntity<>(addressDTO, HttpStatus.FORBIDDEN);
	}
	
	@DeleteMapping(value="/address/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") int id) {
		Optional<Address> address = addressRepository.findById(id);
		if (address.isEmpty()) {
			AddressSuccessMessage result = new AddressSuccessMessage(false);
			return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
		}
		JwtUserDetails jwtUserDetails = (JwtUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        if (jwtUserDetails.getId() == address.get().getUser().getId() || jwtUserDetails.getRole() == UserRole.ROLE_ADMIN) {
        	addressRepository.deleteById(id);
    		AddressSuccessMessage result = new AddressSuccessMessage(true);
    		return new ResponseEntity<>(result, HttpStatus.OK);
		}
        
        AddressDTO addressDTO = new AddressDTO("");
		addressDTO.setForbiddenMessage();
		return new ResponseEntity<>(addressDTO, HttpStatus.FORBIDDEN);
	}
	
}
