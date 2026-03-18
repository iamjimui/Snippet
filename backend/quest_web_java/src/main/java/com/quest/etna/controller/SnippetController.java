package com.quest.etna.controller;

import com.quest.etna.model.Address;
import com.quest.etna.model.AddressDTO;
import com.quest.etna.model.Comment;
import com.quest.etna.model.CommentDTO;
import com.quest.etna.model.Favorite;
import com.quest.etna.model.JwtUserDetails;
import com.quest.etna.model.Sheet;
import com.quest.etna.model.User;
import com.quest.etna.model.Snippet;
import com.quest.etna.model.SnippetDTO;
import com.quest.etna.model.SnippetTag;
import com.quest.etna.model.Tag;
import com.quest.etna.model.UserRole;
import com.quest.etna.repositories.SnippetRepository;
import com.quest.etna.repositories.UserRepository;

import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class SnippetController {

    @Autowired
    private SnippetRepository snippetRepository;

    @PostMapping(value = "/snippet")
    public ResponseEntity<Snippet> addSnippet(@RequestBody Snippet newSnippet){
        snippetRepository.save(newSnippet);
        return new ResponseEntity<Snippet>(newSnippet, HttpStatus.CREATED);
    }
    
    @GetMapping(value = "/snippets/me")
    public @ResponseBody ResponseEntity<List<Snippet>> getByMe(){
    	JwtUserDetails jwtUserDetails = (JwtUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	Iterable<Snippet> snippets = snippetRepository.findByUserId(jwtUserDetails.getId());
        List<Snippet> snippetList = new ArrayList<Snippet>();
        snippets.forEach(snippetList::add);
        return new ResponseEntity<>(snippetList, HttpStatus.OK);
    }

    @GetMapping(value = "/snippets")
    public @ResponseBody ResponseEntity<List<Snippet>> getAll(){
        Iterable<Snippet> snippets = snippetRepository.findAll();
        List<Snippet> snippetList = new ArrayList<Snippet>();
        snippets.forEach(snippetList::add);
        return new ResponseEntity<>(snippetList, HttpStatus.OK);
    }

    @GetMapping(value = "/snippet/{id}")
    public @ResponseBody ResponseEntity<?> getById(@PathVariable("id") Integer id){
        Optional<Snippet> snippet = snippetRepository.findById(id);
        if (snippet.isEmpty()) {
        	SnippetDTO snippetDTO = new SnippetDTO("");
    		snippetDTO.setNotFoundMessage();
			return new ResponseEntity<>(snippetDTO, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(snippet, HttpStatus.OK);
    }

    @DeleteMapping(value = "/snippet/{id}")
    public @ResponseBody ResponseEntity<?> delete(@PathVariable("id") Integer id){
    	Optional<Snippet> existSnippet = snippetRepository.findById(id);
    	
    	if (existSnippet.isEmpty()) {
    		SnippetDTO snippetDTO = new SnippetDTO("");
    		snippetDTO.setNotFoundMessage();
			return new ResponseEntity<>(snippetDTO, HttpStatus.NOT_FOUND);
		}
    	
    	JwtUserDetails jwtUserDetails = (JwtUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	
    	if (jwtUserDetails.getId() == existSnippet.get().getUser().getId() || jwtUserDetails.getRole() == UserRole.ROLE_ADMIN) {
    		snippetRepository.deleteById(id);
    		return new ResponseEntity<>(true, HttpStatus.OK);
    	} else {
    		SnippetDTO snippetDTO = new SnippetDTO("");
    		snippetDTO.setForbiddenMessage();
            return new ResponseEntity<>(snippetDTO, HttpStatus.FORBIDDEN);
    	}
        
    }
    
    @PutMapping(value="/snippets/{id}")
	public ResponseEntity<?> update(@PathVariable("id") int id, @RequestBody Snippet snippet) {
    	System.out.println(snippet.getSheets());
    	Optional<Snippet> existSnippet = snippetRepository.findById(id);
    	JwtUserDetails jwtUserDetails = (JwtUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	if (snippet.getVisible() != null) {
    		existSnippet.get().setVisible(snippet.getVisible());
        }
    	if (snippet.getTitle() != null && !snippet.getTitle().isEmpty()) {
        	existSnippet.get().setTitle(snippet.getTitle());
        }
    	if (snippet.getSheets() != null && !snippet.getSheets().isEmpty()) {
        	existSnippet.get().setSheets(snippet.getSheets());
        }
        if (existSnippet.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		if (jwtUserDetails.getId() == existSnippet.get().getUser().getId() || jwtUserDetails.getRole() == UserRole.ROLE_ADMIN) {
			snippetRepository.save(existSnippet.get());
	        Snippet updatedSnippet = snippetRepository.findById(id).get();
	        return new ResponseEntity<>(updatedSnippet, HttpStatus.OK);
		}
		
		SnippetDTO snippetDTO = new SnippetDTO("");
		snippetDTO.setForbiddenMessage();
        return new ResponseEntity<>(snippetDTO, HttpStatus.FORBIDDEN);
    }

    @PostMapping(value = "/snippetTag")
    public ResponseEntity<?> addSnippetTag(@RequestBody SnippetTag snippetTag) {
        snippetRepository.addSnippetTag(snippetTag.getSnippet_id(), snippetTag.getTag_id());
        return new ResponseEntity<>("true", HttpStatus.OK);
    }
    
    @DeleteMapping(value = "/snippetTag/{id}")
    public @ResponseBody ResponseEntity<?> deleteSnippetTagId(@PathVariable("id") Integer id){
    	snippetRepository.removeSnippetTag(id);
    	return new ResponseEntity<>("true", HttpStatus.OK);
    }
}
