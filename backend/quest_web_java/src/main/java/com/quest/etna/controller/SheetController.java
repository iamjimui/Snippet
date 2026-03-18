package com.quest.etna.controller;

import com.quest.etna.model.Comment;
import com.quest.etna.model.CommentDTO;
import com.quest.etna.model.JwtUserDetails;
import com.quest.etna.model.Sheet;
import com.quest.etna.model.SheetDTO;
import com.quest.etna.model.Snippet;
import com.quest.etna.model.Tag;
import com.quest.etna.model.UserRole;
import com.quest.etna.repositories.SheetRepository;
import com.quest.etna.repositories.SnippetRepository;

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
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SheetController {

    @Autowired
    private SheetRepository sheetRepository;
    
    @Autowired
    private SnippetRepository snippetRepository;
    
    @GetMapping(value = "/sheets")
    public ResponseEntity<?> getAllSheets(){
    	JwtUserDetails jwtUserDetails = (JwtUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	if (jwtUserDetails.getRole() == UserRole.ROLE_ADMIN) {
    		Iterable<Sheet> allSheets = sheetRepository.findAll();
            List<Sheet> sheetList = new ArrayList<Sheet>();
            allSheets.forEach(sheetList::add);
            return new ResponseEntity<>(sheetList, HttpStatus.OK);
    	} else {
    		SheetDTO sheetDTO = new SheetDTO("");
    		sheetDTO.setForbiddenMessage();
            return new ResponseEntity<>(sheetDTO, HttpStatus.FORBIDDEN);
    	}
    }
    
    @GetMapping(value = "/sheet/{id}")
    public ResponseEntity<?> getSheetById(@PathVariable("id") Integer id){
    	JwtUserDetails jwtUserDetails = (JwtUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	if (jwtUserDetails.getRole() == UserRole.ROLE_ADMIN) {
    		Optional<Sheet> existSheet = sheetRepository.findById(id);
    		if (existSheet.isEmpty()) {
    			SheetDTO sheetDTO = new SheetDTO("");
        		sheetDTO.setNotFoundMessage();
                return new ResponseEntity<>(sheetDTO, HttpStatus.FORBIDDEN);
    		}
            return new ResponseEntity<>(existSheet.get(), HttpStatus.OK);
    	} else {
    		SheetDTO sheetDTO = new SheetDTO("");
    		sheetDTO.setForbiddenMessage();
            return new ResponseEntity<>(sheetDTO, HttpStatus.FORBIDDEN);
    	}
    }

    @PostMapping(value = "/sheet")
    public ResponseEntity<Sheet> addSheet(@RequestBody Sheet newSheet){
        sheetRepository.save(newSheet);
        return new ResponseEntity<Sheet>(newSheet, HttpStatus.CREATED);
    }
    
    @DeleteMapping(value = "/sheet/{id}")
    public ResponseEntity<?> deleteSheet(@PathVariable("id") Integer id, @RequestBody Sheet deleteSheet){
    	Optional<Sheet> existSheet = sheetRepository.findById(id);
    	if (existSheet.isEmpty()) {
			return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
		}
    	Optional<Snippet> existSnippet = snippetRepository.findById(existSheet.get().getSnippet().getId());
    	
    	JwtUserDetails jwtUserDetails = (JwtUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	if (jwtUserDetails.getId() == existSnippet.get().getUser().getId() || jwtUserDetails.getRole() == UserRole.ROLE_ADMIN) {
    		sheetRepository.deleteById(id);
            return new ResponseEntity<>(true, HttpStatus.OK);
    	} else {
    		SheetDTO sheetDTO = new SheetDTO("");
    		sheetDTO.setForbiddenMessage();
            return new ResponseEntity<>(sheetDTO, HttpStatus.FORBIDDEN);
    	} 
    }
    
    @PutMapping(value = "/sheet/{id}")
    public ResponseEntity<?> updateSheet(@PathVariable("id") Integer id, @RequestBody Sheet updateSheet){
    	Optional<Sheet> existSheet = sheetRepository.findById(id);
    	if (existSheet.isEmpty()) {
			return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
		}
    	JwtUserDetails jwtUserDetails = (JwtUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	Optional<Snippet> existSnippet = snippetRepository.findById(existSheet.get().getSnippet().getId());
    	if (jwtUserDetails.getId() == existSnippet.get().getUser().getId() || jwtUserDetails.getRole() == UserRole.ROLE_ADMIN) {
    		if (updateSheet.getContent() != null && !updateSheet.getContent().isEmpty()) {
    			existSheet.get().setContent(updateSheet.getContent());
    		}
    		if (updateSheet.getLanguage() != null) {
    			existSheet.get().setLanguage(updateSheet.getLanguage());
    		}
    		if (updateSheet.getName() != null && !updateSheet.getName().isEmpty()) {
    			existSheet.get().setName(updateSheet.getName());
    		}
    		
    		sheetRepository.save(existSheet.get());
            return new ResponseEntity<>(true, HttpStatus.OK);
    	} else {
    		SheetDTO sheetDTO = new SheetDTO("");
    		sheetDTO.setForbiddenMessage();
            return new ResponseEntity<>(sheetDTO, HttpStatus.FORBIDDEN);
    	} 
    }
}
