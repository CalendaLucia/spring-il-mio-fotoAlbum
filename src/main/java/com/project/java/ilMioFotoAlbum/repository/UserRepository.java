package com.project.java.ilMioFotoAlbum.repository;

import com.project.java.ilMioFotoAlbum.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);   //metodo che recupero uno User a partire dall'email (ovvero username)
}
