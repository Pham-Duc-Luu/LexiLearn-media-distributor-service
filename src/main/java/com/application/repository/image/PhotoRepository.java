package com.application.repository.image;

import com.application.model.image.elasticesearch.Photo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface PhotoRepository extends ElasticsearchRepository<Photo, String> {

    List<Photo> findByPhotographerUsername(String username);

    List<Photo> findByPhotoLocationCity(String city);

    List<Photo> findByAiDescriptionContaining(String keyword);
}
