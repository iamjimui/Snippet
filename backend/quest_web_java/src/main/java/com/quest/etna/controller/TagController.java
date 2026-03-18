package com.quest.etna.controller;

import com.quest.etna.model.Address;
import com.quest.etna.model.AddressDTO;
import com.quest.etna.model.AddressSuccessMessage;
import com.quest.etna.model.JwtUserDetails;
import com.quest.etna.model.Tag;
import com.quest.etna.model.TagDTO;
import com.quest.etna.model.TagSuccessMessage;
import com.quest.etna.model.User;
import com.quest.etna.model.UserRole;
import com.quest.etna.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class TagController {
    @Autowired
    private TagRepository tagRepository;

    @GetMapping(value = "/tags")
    public @ResponseBody ResponseEntity<List<Tag>> getAll(){
    	Iterable<Tag> tags = tagRepository.findAll();
        List<Tag> tagList = new ArrayList<Tag>();
        tags.forEach(tagList::add);
        return new ResponseEntity<>(tagList, HttpStatus.OK);
    }
    
    @GetMapping(value = "/tag/{id}")
    public @ResponseBody ResponseEntity<?> getById(@PathVariable("id") int id){
    	Optional<Tag> existTag = tagRepository.findById(id);
    	
    	if (existTag.isEmpty()) {
			TagDTO tagDTO = new TagDTO("");
			tagDTO.setNotFoundMessage();
			return new ResponseEntity<>(tagDTO, HttpStatus.NOT_FOUND);
		}
    	
        return new ResponseEntity<>(existTag.get(), HttpStatus.OK);
    }

    @PostMapping(value = "/tag")
    public ResponseEntity<Tag> addTag(@RequestBody Tag newTag){
        tagRepository.save(newTag);
        return new ResponseEntity<Tag>(newTag, HttpStatus.CREATED);
    }
    
    @PutMapping(value = "/tag/{id}")
    public ResponseEntity<?> updateTag(@PathVariable("id") int id, @RequestBody Tag newTag){
    	Optional<Tag> existTag = tagRepository.findById(id);
    	JwtUserDetails jwtUserDetails = (JwtUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	if (jwtUserDetails.getRole() == UserRole.ROLE_ADMIN) {
    		existTag.get().setName(newTag.getName());
    		tagRepository.save(existTag.get());
	        Tag updatedTag = tagRepository.findById(id).get();
	        return new ResponseEntity<>(updatedTag, HttpStatus.OK);
    	} else {
    		TagDTO tagDTO = new TagDTO("");
    		tagDTO.setForbiddenMessage();
            return new ResponseEntity<>(tagDTO, HttpStatus.FORBIDDEN);
    	}
    }
    
    @DeleteMapping(value="/tag/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") int id) {
		Optional<Tag> tag = tagRepository.findById(id);
		if (tag.isEmpty()) {
			TagSuccessMessage result = new TagSuccessMessage(false);
			return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
		}
		JwtUserDetails jwtUserDetails = (JwtUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        if (jwtUserDetails.getRole() == UserRole.ROLE_ADMIN) {
        	tagRepository.deleteById(id);
        	TagSuccessMessage result = new TagSuccessMessage(true);
    		return new ResponseEntity<>(result, HttpStatus.OK);
		}
        TagDTO tagDTO = new TagDTO("");
		tagDTO.setForbiddenMessage();
        return new ResponseEntity<>(tagDTO, HttpStatus.FORBIDDEN);
	}
}
