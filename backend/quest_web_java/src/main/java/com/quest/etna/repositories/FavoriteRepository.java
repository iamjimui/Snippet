package com.quest.etna.repositories;

import com.quest.etna.model.Favorite;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FavoriteRepository extends CrudRepository<Favorite, Integer> {
	@Modifying
    @Transactional
    @Query(nativeQuery = true, value = "delete from favorite where user_id = :user_id and snippet_id = :snippet_id")
    public void removeFavorite(@Param("user_id") Integer user_id, @Param("snippet_id") Integer snippet_id);
}