package com.application.model.image.elasticesearch;

import com.application.dto.ImageDto;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;

@Data
@Getter
@Setter
public class Photo {
    private IndexCoordinates indexCoordinates = IndexCoordinates.of(PhotoDocument.PHOTOS.getValue());
    @Id
    @Field(name = "_id")
    private String _id;
    @Field(name = "photo_document_name", type = FieldType.Text)
    private String photoDocumentName;
    @Field(name = "_score", type = FieldType.Text)
    private String _score;
    @Field(name = "photo_id", type = FieldType.Text)
    private String photoId;
    @Field(name = "owner_uuid", type = FieldType.Text)
    private String ownerUUID;

    @Field(name = "photo_image_url", type = FieldType.Text)
    private String photoImageUrl;


    @Field(name = "photo_width", type = FieldType.Text)
    private Integer photoWidth;
    @Field(name = "photo_height", type = FieldType.Text)
    private Integer photoHeight;
    @Field(name = "photo_aspect_ratio", type = FieldType.Text)
    private String photoAspectRatio;
    @Field(name = "photo_description", type = FieldType.Text)
    private String photoDescription;


    @Field(name = "ai_description", type = FieldType.Text)
    private String aiDescription;


    public static IndexCoordinates getDefaultIndexCoordinates() {
        return IndexCoordinates.of("photos");
    }

    public static IndexCoordinates getIndexCoordinates(PhotoDocument photoDocument) {
        return IndexCoordinates.of(photoDocument.getValue());
    }

    public ImageDto mapToImageDto() {
        ImageDto imageDto = new ImageDto();
        imageDto.set_id(_id);
        imageDto.setUrl(photoImageUrl);
        imageDto.setDescription(photoDescription);
        imageDto.setWidth(photoWidth);
        imageDto.setHeight(photoHeight);
        imageDto.setOwner_UUID(ownerUUID);
        return imageDto;
    }

    public void setIndexCoordinates(PhotoDocument photoDocument) {
        indexCoordinates = IndexCoordinates.of(photoDocument.getValue());
    }
//
//    public Json getResponseObject() {
//        return
//    }

}
