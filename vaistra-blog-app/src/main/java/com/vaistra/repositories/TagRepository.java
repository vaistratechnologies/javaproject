package com.vaistra.repositories;

import com.vaistra.entities.Category;
import com.vaistra.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {
    Tag findByTagName(String name);
}
