package com.example.application.repository.image;

import com.example.application.model.image.ImageModal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<ImageModal, Integer> {
    Optional<ImageModal> findByFileName(String fileName);
}
