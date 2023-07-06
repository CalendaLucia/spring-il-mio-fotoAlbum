package com.project.java.ilMioFotoAlbum.controller;

import com.project.java.ilMioFotoAlbum.model.Photo;
import com.project.java.ilMioFotoAlbum.services.PhotoServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/files")
public class FileController {
    @Autowired
    PhotoServices photoServices;

    @GetMapping("/cover/{photoId}")
        public ResponseEntity<byte[]> getPhotoCover(@PathVariable Integer photoId) {
        Photo photo = photoServices.getById(photoId);
        MediaType mediaType = MediaType.IMAGE_JPEG;
        return ResponseEntity.ok().contentType(mediaType).body(photo.getUrl());
    }

}
