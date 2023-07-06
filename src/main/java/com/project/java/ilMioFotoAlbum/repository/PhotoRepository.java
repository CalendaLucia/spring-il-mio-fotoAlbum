package com.project.java.ilMioFotoAlbum.repository;

import com.project.java.ilMioFotoAlbum.model.Category;
import com.project.java.ilMioFotoAlbum.model.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo, Integer> {

    List<Photo> findByTitle(String title);

    List<Photo> findByCategoriesIn(List<Category> categories);

}