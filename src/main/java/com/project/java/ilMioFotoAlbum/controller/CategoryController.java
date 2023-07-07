package com.project.java.ilMioFotoAlbum.controller;

import com.project.java.ilMioFotoAlbum.model.Category;
import com.project.java.ilMioFotoAlbum.model.Photo;
import com.project.java.ilMioFotoAlbum.repository.CategoryRepository;
import com.project.java.ilMioFotoAlbum.repository.PhotoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    PhotoRepository photoRepository;

    @GetMapping
    public String index(Model model,
                        @RequestParam("edit")Optional<Integer> categoriesId,
                        @RequestParam(value = "show", defaultValue = "false")
                        boolean show) {
        List<Category> categories;
        categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);

        Category categoryObj;
        if (categoriesId.isPresent()) {
            Optional<Category> categoryDb = categoryRepository.findById(categoriesId.get());
            if (categoryDb.isPresent()) {
                categoryObj = categoryDb.get();
            }else {
                categoryObj = new Category();
            }
        }else {
            categoryObj = new Category();
        }

        model.addAttribute("categoryObj", categoryObj);
        model.addAttribute("show", show);
        return "/categories/categories";

    }


    //controller per gestire la creazione delle categorie
    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("categoryObj") Category formCategory,
                         BindingResult bindingResult,Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryRepository.findAll());
            return "/categories/categories";
        }
        categoryRepository.save(formCategory);
        return "redirect:/categories";
    }

    //Controller per eliminazione categorie

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        Optional<Category> result = categoryRepository.findById(id);
        if (result.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        Category categoryToDelete = result.get();
        for (Photo photo : categoryToDelete.getPhotoList()) {
            photo.getCategories().remove(categoryToDelete);
        }
        categoryRepository.deleteById(id);
        return "redirect:/categories";
    }
}
