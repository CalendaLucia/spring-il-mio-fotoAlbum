package com.project.java.ilMioFotoAlbum.api;

import com.project.java.ilMioFotoAlbum.model.Photo;
import com.project.java.ilMioFotoAlbum.repository.CategoryRepository;
import com.project.java.ilMioFotoAlbum.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("api/v1/photos")
public class PhotoRestController {
    @Autowired
    private PhotoRepository photoRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    // ottenere la lista di foto (dobbiamo anche poterle filtrare per titolo)
    @GetMapping
    public List<Photo> getPhotos(@RequestParam(name = "keyword", required = false) String search) {

        List<Photo> photos;
        if (search == null || search.isBlank()) {
            // se non ho il parametro search faccio la query generica
            photos = photoRepository.findAll();    // recupero la lista di libri dal database
        } else {
            // se ho il parametro search faccio la query con filtro
            photos = photoRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrCategories_Name(search,search,search);
        }
        return photos;
    }



}
