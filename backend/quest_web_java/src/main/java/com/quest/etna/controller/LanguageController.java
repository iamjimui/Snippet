package com.quest.etna.controller;

import com.quest.etna.model.AddressDTO;
import com.quest.etna.model.JwtUserDetails;
import com.quest.etna.model.Language;
import com.quest.etna.model.LanguageDTO;
import com.quest.etna.model.LanguageSuccessMessage;
import com.quest.etna.model.SnippetDTO;
import com.quest.etna.model.Tag;
import com.quest.etna.model.TagDTO;
import com.quest.etna.model.TagSuccessMessage;
import com.quest.etna.model.UserRole;
import com.quest.etna.repositories.LanguageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class LanguageController {

    @Autowired
    private LanguageRepository languageRepository;

    @GetMapping(value = "/languages")
    public @ResponseBody ResponseEntity<List<Language>> getAll(){
        Iterable<Language> languages = languageRepository.findAll();
        List<Language> languageList = new ArrayList<Language>();
        languages.forEach(languageList::add);
        return new ResponseEntity<>(languageList, HttpStatus.OK);
    }
    
    @GetMapping(value = "/language/{id}")
    public @ResponseBody ResponseEntity<?> getById(@PathVariable("id") int id){
    	Optional<Language> existLanguage = languageRepository.findById(id);
    	
    	if (existLanguage.isEmpty()) {
			LanguageDTO languageDTO = new LanguageDTO("");
			languageDTO.setNotFoundMessage();
			return new ResponseEntity<>(languageDTO, HttpStatus.NOT_FOUND);
		}
    	
        return new ResponseEntity<>(existLanguage.get(), HttpStatus.OK);
    }

    @PostMapping(value = "/language")
    public ResponseEntity<?> addLanguage(@RequestBody Language newLanguage){
    	JwtUserDetails jwtUserDetails = (JwtUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        if (jwtUserDetails.getRole() == UserRole.ROLE_ADMIN) {
        	languageRepository.save(newLanguage);
            return new ResponseEntity<Language>(newLanguage, HttpStatus.CREATED);
        }
        
        LanguageDTO languageDTO = new LanguageDTO("");
        languageDTO.setForbiddenMessage();
		return new ResponseEntity<>(languageDTO, HttpStatus.FORBIDDEN);
    }
    
    @PutMapping(value = "/language/{id}")
    public ResponseEntity<?> updateLanguage(@PathVariable("id") int id, @RequestBody Tag newLanguage){
    	Optional<Language> existLanguage = languageRepository.findById(id);
    	if (existLanguage.isEmpty()) {
    		LanguageDTO languageDTO = new LanguageDTO("");
    		languageDTO.setNotFoundMessage();
			return new ResponseEntity<>(languageDTO, HttpStatus.NOT_FOUND);
		}
    	JwtUserDetails jwtUserDetails = (JwtUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	if (jwtUserDetails.getRole() == UserRole.ROLE_ADMIN) {
    		existLanguage.get().setName(newLanguage.getName());
    		languageRepository.save(existLanguage.get());
	        Language updatedLanguage = languageRepository.findById(id).get();
	        return new ResponseEntity<>(updatedLanguage, HttpStatus.OK);
    	} else {
    		LanguageDTO languageDTO = new LanguageDTO("");
    		languageDTO.setForbiddenMessage();
            return new ResponseEntity<>(languageDTO, HttpStatus.FORBIDDEN);
    	}
    }
    
    @DeleteMapping(value="/language/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") int id) {
		Optional<Language> existLanguage = languageRepository.findById(id);
		if (existLanguage.isEmpty()) {
			LanguageSuccessMessage result = new LanguageSuccessMessage(false);
			return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
		}
		JwtUserDetails jwtUserDetails = (JwtUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        if (jwtUserDetails.getRole() == UserRole.ROLE_ADMIN) {
        	languageRepository.deleteById(id);
        	LanguageSuccessMessage result = new LanguageSuccessMessage(true);
    		return new ResponseEntity<>(result, HttpStatus.OK);
		}
        LanguageDTO languageDTO = new LanguageDTO("");
		languageDTO.setForbiddenMessage();
        return new ResponseEntity<>(languageDTO, HttpStatus.FORBIDDEN);
	}
}
