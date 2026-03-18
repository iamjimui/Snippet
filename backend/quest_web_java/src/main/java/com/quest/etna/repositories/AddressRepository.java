package com.quest.etna.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.quest.etna.model.Address;

@Repository
public interface AddressRepository extends CrudRepository<Address, Integer> {
	@Query(nativeQuery=true, value="select * from address where user_id = :id")
	public List<Address> findByUserId(@Param("id") int id);

}
