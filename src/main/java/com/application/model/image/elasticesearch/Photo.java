package com.application.model.image.elasticesearch;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "photos")
@Data
public class Photo {


    @Id
    @Field(name = "_id")
    private String _id;
    
    @Field(name = "_score", type = FieldType.Text)
    private String _score;

    @Field(name = "photo_id", type = FieldType.Text)
    private String photoId;

    @Field(name = "photo_url", type = FieldType.Text)
    private String photoUrl;

    @Field(name = "photo_image_url", type = FieldType.Text)
    private String photoImageUrl;

    @Field(name = "photo_submitted_at", type = FieldType.Text)
    private String photoSubmittedAt;

    @Field(name = "photo_featured", type = FieldType.Text)
    private String photoFeatured;

    @Field(name = "photo_width", type = FieldType.Text)
    private String photoWidth;

    @Field(name = "photo_height", type = FieldType.Text)
    private String photoHeight;

    @Field(name = "photo_aspect_ratio", type = FieldType.Text)
    private String photoAspectRatio;

    @Field(name = "photo_description", type = FieldType.Text)
    private String photoDescription;

    @Field(name = "photographer_username", type = FieldType.Text)
    private String photographerUsername;

    @Field(name = "photographer_first_name", type = FieldType.Text)
    private String photographerFirstName;

    @Field(name = "photographer_last_name", type = FieldType.Text)
    private String photographerLastName;

    @Field(name = "exif_camera_make", type = FieldType.Text)
    private String exifCameraMake;

    @Field(name = "exif_camera_model", type = FieldType.Text)
    private String exifCameraModel;

    @Field(name = "exif_iso", type = FieldType.Text)
    private String exifIso;

    @Field(name = "exif_aperture_value", type = FieldType.Text)
    private String exifApertureValue;

    @Field(name = "exif_focal_length", type = FieldType.Text)
    private String exifFocalLength;

    @Field(name = "exif_exposure_time", type = FieldType.Text)
    private String exifExposureTime;

    @Field(name = "photo_location_name", type = FieldType.Text)
    private String photoLocationName;

    @Field(name = "photo_location_latitude", type = FieldType.Text)
    private String photoLocationLatitude;

    @Field(name = "photo_location_longitude", type = FieldType.Text)
    private String photoLocationLongitude;

    @Field(name = "photo_location_country", type = FieldType.Text)
    private String photoLocationCountry;

    @Field(name = "photo_location_city", type = FieldType.Text)
    private String photoLocationCity;

    @Field(name = "stats_views", type = FieldType.Text)
    private String statsViews;

    @Field(name = "stats_downloads", type = FieldType.Text)
    private String statsDownloads;

    @Field(name = "ai_description", type = FieldType.Text)
    private String aiDescription;

    @Field(name = "ai_primary_landmark_name", type = FieldType.Text)
    private String aiPrimaryLandmarkName;

    @Field(name = "ai_primary_landmark_latitude", type = FieldType.Text)
    private String aiPrimaryLandmarkLatitude;

    @Field(name = "ai_primary_landmark_longitude", type = FieldType.Text)
    private String aiPrimaryLandmarkLongitude;

    @Field(name = "ai_primary_landmark_confidence", type = FieldType.Text)
    private String aiPrimaryLandmarkConfidence;

    @Field(name = "blur_hash", type = FieldType.Text)
    private String blurHash;
}
