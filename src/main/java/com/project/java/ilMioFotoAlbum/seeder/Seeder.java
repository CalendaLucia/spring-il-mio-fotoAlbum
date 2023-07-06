package com.project.java.ilMioFotoAlbum.seeder;

import com.project.java.ilMioFotoAlbum.model.Photo;
import com.project.java.ilMioFotoAlbum.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Component
public class Seeder implements CommandLineRunner {
    @Autowired
    PhotoRepository photoRepository;

    @Override
    public void run(String... args) throws Exception {

        File directory = new ClassPathResource("seeder_images").getFile();
        List<Photo> images = new ArrayList<>();
        for (File file : directory.listFiles()) {
            try {
                Photo img = new Photo();
                byte[] bytes = Files.readAllBytes(Path.of(file.getPath()));
                img.setUrl(bytes);
                images.add(img);
            }catch (IOException e) {
                System.out.println("unable to read file");
            }
        }
        photoRepository.saveAll(images);
    }

}
