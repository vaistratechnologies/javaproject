package com.vaistra.repositories;

import com.vaistra.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Category findByCategoryName(String name);

    @Query(value = "SELECT * from category c WHERE c.is_deleted = 1 AND c.category_id=:id", nativeQuery = true)
    Category findByIdTrashed(Integer id);

    @Query(value = "SELECT * from category c WHERE c.is_active = 0 AND c.category_id=:id", nativeQuery = true)
    Category findByIdInActive(Integer id);

    @Query(value = "SELECT * from category c WHERE c.is_deleted = 1", nativeQuery = true)
    List<Category> findAllTrashed();

    @Query(value = "SELECT * from category c WHERE c.is_active = 0 AND c.is_deleted=0", nativeQuery = true)
    List<Category> findAllInActive();
}
