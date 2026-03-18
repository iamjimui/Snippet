package com.quest.etna.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.quest.etna.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
	@Query(nativeQuery=true, value="select * from user where username = :username")
	public Optional<User> findByUsername(@Param("username") String username);

	@Query(nativeQuery=true, value="select * from user where password = :password")
	public Optional<User> findByPassword(@Param("password") String password);

	@Query(nativeQuery=true, value="select * from user where email = :email")
	public Optional<User> findByEmail(@Param("email") String email);

}