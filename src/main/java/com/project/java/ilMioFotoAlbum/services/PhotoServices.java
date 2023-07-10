package com.project.java.ilMioFotoAlbum.services;

import com.project.java.ilMioFotoAlbum.dto.PhotoDto;
import com.project.java.ilMioFotoAlbum.model.Photo;
import com.project.java.ilMioFotoAlbum.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class PhotoServices {

@Autowired
    PhotoRepository photoRepository;


    // Metodo privato per convertire un oggetto MultipartFile in un array di byte.
    private byte[] multipartFileToByteArray(MultipartFile mpf) {
        byte[] bytes = null;
        if (mpf != null && !mpf.isEmpty()) {
            try {
                // Ottiene l'array di byte dal MultipartFile
                bytes = mpf.getBytes();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return bytes;
    }

    private Photo mapFormPhotoToPhoto(PhotoDto formPhoto) {
        Photo photo = new Photo();
        photo.setId(formPhoto.getId());
        photo.setTitle(formPhoto.getTitle());
        photo.setDescription(formPhoto.getDescription());
        photo.setVisible(formPhoto.isVisible());
        photo.setCategories(formPhoto.getCategories());
        photo.setUrl(multipartFileToByteArray(formPhoto.getCoverFile()));
        return photo;
    }

    private PhotoDto mapPhotoToFormPhoto(Photo photo) {
        PhotoDto  formPhoto = new PhotoDto();
        formPhoto.setId(photo.getId());
        formPhoto.setTitle(photo.getTitle());
        formPhoto.setDescription(photo.getDescription());
        formPhoto.setVisible(photo.isVisible());
        formPhoto.setCategories(photo.getCategories());
        return formPhoto;
    }

    //metodo che restituisce un photo preso per id
    public Photo getById(Integer id) throws ResponseStatusException {
        Optional<Photo> photoOptional = photoRepository.findById(id);
        if (photoOptional.isPresent()){
            return  photoOptional.get();
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    //metodo per creare una nuova foto
    public Photo create(Photo photo) {
        //creo la foto da salvare
        Photo photoToPersist = new Photo();
        photoToPersist.setTitle(photo.getTitle());
        photoToPersist.setDescription(photo.getDescription());
        photoToPersist.setUrl(photo.getUrl());
        photoToPersist.setVisible(photo.isVisible());
        photoToPersist.setCategories(photo.getCategories());
        return photoRepository.save(photoToPersist);
    }

    //metodo che crea una nuova foto a partire da una PhotoDto
    public  Photo create(PhotoDto formPhoto) {
        Photo photo = mapFormPhotoToPhoto(formPhoto);
        return create(photo);
    }



    //metodo per creare un formPhoto a partire dall'id di una photo salvato su db
    public PhotoDto getPhotoFormById(Integer id) {
        Photo photo = getById(id);
        return mapPhotoToFormPhoto(photo);
    }

    //metodo update per form
    public Photo update(PhotoDto formPhoto) {
        Photo photo = mapFormPhotoToPhoto(formPhoto);
        Photo photoDb = getById(photo.getId());
        photoDb.setTitle(photo.getTitle());
        photoDb.setDescription(photo.getDescription());
        photoDb.setVisible(photo.isVisible());
        photoDb.setCategories(photo.getCategories());
        return photoRepository.save(photoDb);
    }

    public Photo setPhotoVisibility(Integer photoId,boolean visibility) {
        Optional<Photo> optionalPhoto = photoRepository.findById(photoId);

        if (optionalPhoto.isPresent()) {
            Photo photo = optionalPhoto.get();
            photo.setVisible(visibility);
            return   photoRepository.save(photo);

        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

    }

    public List<Photo> getVisiblePhotos() {
        return photoRepository.findByVisible(true);
    }








}

