package com.project.java.ilMioFotoAlbum.controller;
import com.project.java.ilMioFotoAlbum.dto.PhotoDto;
import com.project.java.ilMioFotoAlbum.messages.AlertMessage;
import com.project.java.ilMioFotoAlbum.messages.AlertMessageType;
import com.project.java.ilMioFotoAlbum.model.Category;
import com.project.java.ilMioFotoAlbum.model.Photo;
import com.project.java.ilMioFotoAlbum.repository.CategoryRepository;
import com.project.java.ilMioFotoAlbum.repository.PhotoRepository;
import com.project.java.ilMioFotoAlbum.services.PhotoServices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.naming.AuthenticationException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/photos")
public class PhotoController {

    @Autowired
    PhotoRepository photoRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    PhotoServices photoServices;

    //lettura dati da database
    @GetMapping
    public String index(@RequestParam(name = "keyword", required = false) String search,
                        Authentication authentication,
                        Model model) {
        List<Photo> photos;
        if (search == null || search.isBlank()) {
            photos = photoRepository.findAll();

        } else {
            photos = photoRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrCategories_Name(search, search, search);

        }
        if (photos.isEmpty()) {
            model.addAttribute("message", "Sorry, catalog is empty");
        }
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            model.addAttribute("username", username);
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

    //Controller che gestisce la creazione del form per l'inserimento di una nuova foto

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("photo", new PhotoDto());
        model.addAttribute("categories", categoryRepository.findAll());
        return "photos/create";
    }

    //Controller che gestisce la post del form coi dati della nuova Foto

    @PostMapping("/create")
    public String store(@Valid @ModelAttribute("photo")PhotoDto formPhoto,
                        @RequestParam("selectedCategoriesIds") List<Integer> categoriesIds,
                        BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return "photos/create";
        }

        // Recupero gli ingredienti selezionati dal repository delle categorie usando gli ID
        List<Category> selectedCategories = categoryRepository.findAllById(categoriesIds);
        //imposto le categorie selezionate sulla foto
        formPhoto.setCategories(selectedCategories);
        //chiedo a photoservice di salvare su db una foto a partire da formPhoto
        Photo createdPhoto = photoServices.create(formPhoto);
        return "redirect:/photos";

    }

    //EDIT Controller che gestisce la modifica dei dati del form
    @GetMapping("/edit/{id}")
    public String edit (@PathVariable("id") Integer id, Model model){
        try {
            //recupero i dati di quella foto da database
            PhotoDto formPhoto = photoServices.getPhotoFormById(id);

            //aggiungo la foto al model
            model.addAttribute("photo", formPhoto);
            //aggiungo la lista delle categorie al model
            model.addAttribute("categories", categoryRepository.findAll());


            return "/photos/create";
        } catch (ResponseStatusException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    //UPDATE Controller che gestisce la post delle modifiche nel form

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Integer id,
                         @RequestParam("selectedCategoriesIds") List<Integer> categoriesIds,
                         @RequestParam(value = "coverFile") MultipartFile coverFile,
                         @Valid @ModelAttribute("photo") PhotoDto formPhoto,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes){
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("message", new AlertMessage(AlertMessageType.ERROR, "Sorry, but we couldn't apply the modification!"));
            return "photos/create";
        }

        try {
               photoServices.update(formPhoto);

        }catch (ResponseStatusException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        //Recupero le categorie selezionate dal repository delle categorie usando gli id
        List<Category> selectedCategories = categoryRepository.findAllById(categoriesIds);
        //imposto le categorie alla foto
        formPhoto.setCategories(selectedCategories);

        //mando una conferma di successo
        redirectAttributes.addFlashAttribute("message", new AlertMessage(AlertMessageType.SUCCESS, "Photo updated!!"));
        return "redirect:/photos";

    }

    //Controller delete
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        Photo photoToDelete = getPhotoById(id);
        photoRepository.delete(photoToDelete);
        redirectAttributes.addFlashAttribute("message", new AlertMessage(AlertMessageType.SUCCESS, "Pizza" + photoToDelete.getTitle() + " deleted success"));
        return "redirect:/photos";
    }

    //metodo per selezionare l oggetto da database e tirare un eccezione
    private Photo getPhotoById(Integer id) {
        Optional<Photo> result = photoRepository.findById(id);
        if (result.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Photo" + id + " not found");
        }
        return result.get();
    }




}






