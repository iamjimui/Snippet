package com.quest.etna.repositories;

import com.quest.etna.model.Sheet;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SheetRepository extends CrudRepository<Sheet, Integer> {
}
