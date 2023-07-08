package com.project.java.ilMioFotoAlbum.api;

import com.project.java.ilMioFotoAlbum.model.Photo;
import com.project.java.ilMioFotoAlbum.repository.CategoryRepository;
import com.project.java.ilMioFotoAlbum.repository.PhotoRepository;
import com.project.java.ilMioFotoAlbum.services.PhotoServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("api/v1/photos")
public class PhotoRestController {
    @Autowired
    private PhotoRepository photoRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PhotoServices photoServices;

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

    @GetMapping("/{photoId}")
    public ResponseEntity<Photo> getPhoto(@PathVariable Integer photoId) {
        Optional<Photo> result = photoRepository.findById(photoId);
        if (result.isPresent()){
            Photo photo = result.get();
            return ResponseEntity.ok(photo);
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }



}
