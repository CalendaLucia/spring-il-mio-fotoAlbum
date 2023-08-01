package com.project.java.ilMioFotoAlbum.api;

import com.project.java.ilMioFotoAlbum.model.Category;
import com.project.java.ilMioFotoAlbum.model.ContactMessage;
import com.project.java.ilMioFotoAlbum.model.Photo;
import com.project.java.ilMioFotoAlbum.repository.CategoryRepository;
import com.project.java.ilMioFotoAlbum.repository.ContactMessageRepository;
import com.project.java.ilMioFotoAlbum.repository.PhotoRepository;
import com.project.java.ilMioFotoAlbum.services.PhotoServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Autowired
    private ContactMessageRepository contactMessageRepository;

    // ottenere la lista di foto (dobbiamo anche poterle filtrare per titolo)
    @GetMapping
    public List<Photo> getPhotos(@RequestParam(name = "keyword", required = false) String search,
                                 @RequestParam(name = "visible", required = false) Boolean visible) {

        List<Photo> photos;
        if (search == null || search.isBlank()) {
            // se non ho il parametro search faccio la query generica
            photos = photoRepository.findAll();    // recupero la lista di libri dal database
        } else {
            // se ho il parametro search faccio la query con filtro
            photos = photoRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrCategories_Name(search,search,search);
        }

        if (visible != null && visible) {
            photos = photos.stream()
                    .filter(Photo::isVisible)
                    .collect(Collectors.toList());
        }
        return photos;
    }


    @GetMapping("/{photoId}/image")
    public ResponseEntity<byte[]> getPhotoImage(@PathVariable Integer photoId) {
        Optional<Photo> result = photoRepository.findById(photoId);
        if (result.isPresent()) {
            Photo photo = result.get();
            byte[] imageBytes = photo.getUrl(); // Assume che i dati binari dell'immagine siano disponibili come un array di byte nel modello Photo
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG); // Imposta il tipo di contenuto dell'immagine, modifica se necessario

            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
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


    //recupero da database email e messaggio inviati da frontend
    @GetMapping("/contact-messages")
    public List<ContactMessage> getAllContactMessages() {
        return contactMessageRepository.findAll();
    }

    //salvo nel database email e messaggio inviati da un utente su frontend
    @PostMapping("/submit-form")
    public void submitForm(@RequestBody ContactMessage contactMessage) {
        contactMessageRepository.save(contactMessage);
    }


    //recupero le categorie da sole

    @GetMapping("/categories")
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();

    }

}
