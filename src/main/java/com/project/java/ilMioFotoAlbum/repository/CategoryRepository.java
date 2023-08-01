package com.project.java.ilMioFotoAlbum.repository;

import com.project.java.ilMioFotoAlbum.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    List<Category> findByName(String name);

}
