package com.project.java.ilMioFotoAlbum.repository;

import com.project.java.ilMioFotoAlbum.model.Category;
import com.project.java.ilMioFotoAlbum.model.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo, Integer> {

    List<Photo> findByTitle(String title);

    List<Photo> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrCategories_Name(String title,
                                                                                                  String description,
                                                                                                  String categoryName);





}