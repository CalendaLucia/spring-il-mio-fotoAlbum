package com.project.java.ilMioFotoAlbum.seeder;

import com.project.java.ilMioFotoAlbum.model.Category;
import com.project.java.ilMioFotoAlbum.model.Photo;
import com.project.java.ilMioFotoAlbum.repository.CategoryRepository;
import com.project.java.ilMioFotoAlbum.repository.PhotoRepository;
import com.project.java.ilMioFotoAlbum.services.PhotoServices;
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

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    PhotoServices photoServices;

    // Classe che implementa CommandLineRunner per eseguire un seed custom lato java:
    @Override
    public void run(String... args) throws Exception {

        File directory = new ClassPathResource("seeder_images").getFile();
        List<Photo> images = new ArrayList<>();
        List<Category> categories = new ArrayList<>();

        Category category1= new Category();
        category1.setName("Nature");
        categories.add(category1);

        Category category2 = new Category();
        category2.setName("Peace");
        categories.add(category2);

        for (File file : directory.listFiles()) {
            try {
                Photo img = new Photo();
                byte[] bytes = Files.readAllBytes(Path.of(file.getPath()));
                img.setUrl(bytes);
                img.setTitle("All you need is win");
                img.setDescription("description photo");
                img.setCategories(categories);
                images.add(img);
            }catch (IOException e) {
                System.out.println("unable to read file");
            }
        }
        categoryRepository.saveAll(categories);
        photoRepository.saveAll(images);
    }

}
