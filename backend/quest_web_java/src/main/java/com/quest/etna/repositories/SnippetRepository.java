package com.quest.etna.repositories;

import com.quest.etna.model.Address;
import com.quest.etna.model.Snippet;
import com.quest.etna.model.SnippetTag;
import com.quest.etna.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SnippetRepository extends CrudRepository<Snippet, Integer> {
	@Query(nativeQuery=true, value="select * from snippet where user_id = :id")
	public List<Snippet> findByUserId(@Param("id") int id);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "INSERT INTO snippet_tag (`snippet_id`, `tag_id`) VALUES (:snippet_id, :tag_id)")
    public void addSnippetTag(@Param("snippet_id") Integer snippet_id, @Param("tag_id") Integer tag_id);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "delete from snippet_tag where snippet_id = :id")
    public void removeSnippetTag(@Param("id") Integer id);
}