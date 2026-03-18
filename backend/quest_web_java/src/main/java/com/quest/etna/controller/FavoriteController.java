package com.quest.etna.controller;

import com.quest.etna.model.Address;
import com.quest.etna.model.Comment;
import com.quest.etna.model.CommentDTO;
import com.quest.etna.model.Favorite;
import com.quest.etna.model.FavoriteDTO;
import com.quest.etna.model.JwtUserDetails;
import com.quest.etna.model.Snippet;
import com.quest.etna.model.SnippetDTO;
import com.quest.etna.model.UserRole;
import com.quest.etna.repositories.FavoriteRepository;
import com.quest.etna.repositories.SnippetRepository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FavoriteController {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @PostMapping(value = "/favorite")
    public ResponseEntity<Favorite> addFavorite(@RequestBody Favorite newFavorite) {
        System.out.println(newFavorite);
        favoriteRepository.save(newFavorite);
        return new ResponseEntity<Favorite>(newFavorite, HttpStatus.CREATED);
    }
    
    @GetMapping(value = "/favorite/{id}")
    public ResponseEntity<?> getFavoriteById(@PathVariable("id") Integer id) {
    	Optional<Favorite> existFavorite = favoriteRepository.findById(id);
        if (existFavorite.isEmpty()) {
        	FavoriteDTO favoriteDTO = new FavoriteDTO("");
        	favoriteDTO.setNotFoundMessage();
			return new ResponseEntity<>(favoriteDTO, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(existFavorite, HttpStatus.OK);
    }
    
    @DeleteMapping(value = "/favorite/{id}")
    public ResponseEntity<?> deleteFavorite(@PathVariable("id") Integer id){
    	Optional<Favorite> existFavorite = favoriteRepository.findById(id);
    	if (existFavorite.isEmpty()) {
			return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
		}
    	JwtUserDetails jwtUserDetails = (JwtUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	if (jwtUserDetails.getId() == existFavorite.get().getUser().getId() || jwtUserDetails.getRole() == UserRole.ROLE_ADMIN) {
    		favoriteRepository.deleteById(id);
            return new ResponseEntity<>(true, HttpStatus.OK);
    	} else {
    		FavoriteDTO favoriteDTO = new FavoriteDTO("");
    		favoriteDTO.setForbiddenMessage();
            return new ResponseEntity<>(favoriteDTO, HttpStatus.FORBIDDEN);
    	} 
    }
    
    @DeleteMapping(value = "/favorite/{user_id}/{snippet_id}")
    public ResponseEntity<?> deleteFavoriteByUserIdAndSnippetId(@PathVariable("user_id") Integer user_id, @PathVariable("snippet_id") Integer snippet_id){
    	JwtUserDetails jwtUserDetails = (JwtUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	if (jwtUserDetails.getId() == user_id || jwtUserDetails.getRole() == UserRole.ROLE_ADMIN) {
    		favoriteRepository.removeFavorite(user_id, snippet_id);
            return new ResponseEntity<>(true, HttpStatus.OK);
    	}
    	FavoriteDTO favoriteDTO = new FavoriteDTO("");
		favoriteDTO.setForbiddenMessage();
        return new ResponseEntity<>(favoriteDTO, HttpStatus.FORBIDDEN);
    }
}
