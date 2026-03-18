package com.quest.etna.controller;

import com.quest.etna.model.Address;
import com.quest.etna.model.AddressDTO;
import com.quest.etna.model.AddressSuccessMessage;
import com.quest.etna.model.Comment;
import com.quest.etna.model.CommentDTO;
import com.quest.etna.model.JwtUserDetails;
import com.quest.etna.model.Language;
import com.quest.etna.model.LanguageDTO;
import com.quest.etna.model.Sheet;
import com.quest.etna.model.Snippet;
import com.quest.etna.model.User;
import com.quest.etna.model.UserRole;
import com.quest.etna.repositories.CommentRepository;
import com.quest.etna.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class CommentController {

    @Autowired
    CommentRepository commentRepository;

    @PostMapping(value = "/comment")
    public ResponseEntity<Comment> addComment(@RequestBody Comment newComment){
        commentRepository.save(newComment);
        return new ResponseEntity<Comment>(newComment, HttpStatus.CREATED);
    }

    @GetMapping(value = "/comments")
    public @ResponseBody ResponseEntity<?> getAll(){
        Iterable<Comment> comments = commentRepository.findAll();
        List<Comment> commentList = new ArrayList<Comment>();
        comments.forEach(commentList::add);
        return new ResponseEntity<>(commentList, HttpStatus.OK);
    }
    
    @GetMapping(value = "/comment/{id}")
    public @ResponseBody ResponseEntity<?> getById(@PathVariable("id") int id){
        Optional<Comment> existComment = commentRepository.findById(id);
    	
    	if (existComment.isEmpty()) {
			CommentDTO commentDTO = new CommentDTO("");
			commentDTO.setNotFoundMessage();
			return new ResponseEntity<>(commentDTO, HttpStatus.NOT_FOUND);
		}
        return new ResponseEntity<>(existComment.get(), HttpStatus.OK);
    }
    
    @GetMapping(value = "/comments/{id}")
    public @ResponseBody ResponseEntity<?> getByUserId(@PathVariable("id") int id){
        Iterable<Comment> comments = commentRepository.findByUserId(id);
        List<Comment> commentList = new ArrayList<Comment>();
        comments.forEach(commentList::add);
        return new ResponseEntity<>(commentList, HttpStatus.OK);
    }

    @DeleteMapping(value = "/comment/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable("id") Integer id){
    	Optional<Comment> existComment = commentRepository.findById(id);
    	if (existComment.isEmpty()) {
			return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
		}
    	JwtUserDetails jwtUserDetails = (JwtUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	if (jwtUserDetails.getId() == existComment.get().getUser().getId() || jwtUserDetails.getRole() == UserRole.ROLE_ADMIN) {
    		commentRepository.deleteById(id);
            return new ResponseEntity<>(true, HttpStatus.OK);
    	} else {
    		CommentDTO commentDTO = new CommentDTO("");
    		commentDTO.setForbiddenMessage();
            return new ResponseEntity<>(commentDTO, HttpStatus.FORBIDDEN);
    	} 
    }
    
    @PutMapping(value="/comment/{id}")
    public ResponseEntity<?> updateComment(@PathVariable("id") Integer id, @RequestBody Comment newComment){
    	Optional<Comment> existComment = commentRepository.findById(id);
    	if (existComment.isEmpty()) {
			return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
		}
    	JwtUserDetails jwtUserDetails = (JwtUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	if (jwtUserDetails.getId() == existComment.get().getUser().getId() || jwtUserDetails.getRole() == UserRole.ROLE_ADMIN) {
    		existComment.get().setMessage(newComment.getMessage());
    		commentRepository.save(existComment.get());
            return new ResponseEntity<>(true, HttpStatus.OK);
    	} else {
    		CommentDTO commentDTO = new CommentDTO("");
    		commentDTO.setForbiddenMessage();
            return new ResponseEntity<>(commentDTO, HttpStatus.FORBIDDEN);
    	} 
    }
}
