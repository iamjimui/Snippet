package com.quest.etna.repositories;

import com.quest.etna.model.Comment;
import com.quest.etna.model.User;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Integer> {
	@Query(nativeQuery=true, value="select * from comment where user_id = :id order by created_date desc")
	public List<Comment> findByUserId(@Param("id") Integer id);

}
