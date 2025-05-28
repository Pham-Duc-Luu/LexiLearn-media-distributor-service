package com.application.dto;

import com.application.model.mongo.CollectingImageModal;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ImageDto {
    private String _id;
    private String owner_UUID;
    private String url;
    private Integer width;
    private Integer height;
    private String description;
    private String title;

    public CollectingImageModal mapToCollectionImageModal() {
        CollectingImageModal collectingImageModal = new CollectingImageModal();

        collectingImageModal.setId(this._id);
        collectingImageModal.setLink(this.url);
        collectingImageModal.setTitle(this.title);
        collectingImageModal.setHeight(this.height);
        collectingImageModal.setWidth(this.width);
        // You can compute or infer format and byteSize if applicable
        collectingImageModal.setFileFormat(inferFileFormat(this.url));
        // Optional: collectingImageModal.setByteSize(...);

        return collectingImageModal;
    }

    private String inferFileFormat(String url) {
        if (url == null || !url.contains(".")) return null;
        return url.substring(url.lastIndexOf('.') + 1).toLowerCase();
    }
}