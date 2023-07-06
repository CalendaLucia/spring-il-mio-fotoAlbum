package com.project.java.ilMioFotoAlbum.repository;

import com.project.java.ilMioFotoAlbum.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Category findByName(String name);
}
