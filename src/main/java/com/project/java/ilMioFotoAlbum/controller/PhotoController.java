package com.project.java.ilMioFotoAlbum.controller;

import com.project.java.ilMioFotoAlbum.model.Category;
import com.project.java.ilMioFotoAlbum.model.Photo;
import com.project.java.ilMioFotoAlbum.repository.CategoryRepository;
import com.project.java.ilMioFotoAlbum.repository.PhotoRepository;
import com.project.java.ilMioFotoAlbum.services.PhotoServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/photos")
public class PhotoController {

    @Autowired
    PhotoRepository photoRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    PhotoServices photoServices;

    @GetMapping
    public String index(@RequestParam(name = "keyword", required = false) String search,
                        Model model) {
        List<Photo> photos;
        if (search == null || search.isBlank()) {
            photos = photoRepository.findAll();
        } else {
            Category category = categoryRepository.findByName(search);

            if (category != null) {
                photos = photoRepository.findByCategoriesIn(Collections.singletonList(category));
            }else {
                photos = new ArrayList<>();
            }
        }

        model.addAttribute("photos", photos);
        model.addAttribute("search",search == null ? "" : search);
        return "photos/index";
    }

    //controller per ricerca singolo Id
    @GetMapping("/{id}")
    public String show(@PathVariable("id") Integer photoId, Model model) {
        Photo photo = photoServices.getById(photoId);
        List<Category> categories = photo.getCategories();
        model.addAttribute("photo", photo);
        model.addAttribute("categories", categories);

        return "photos/details";
    }


}






