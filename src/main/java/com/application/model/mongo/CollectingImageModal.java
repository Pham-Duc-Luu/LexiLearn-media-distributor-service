package com.application.model.mongo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "collection_image_info")
public class CollectingImageModal {
    @Id
    private String id;

    @Indexed(unique = true)
    private String title;

    @Indexed(unique = true)
    private String link;

    private String fileFormat;

    private Integer height;
    private Integer width;
    private Integer byteSize;

}
