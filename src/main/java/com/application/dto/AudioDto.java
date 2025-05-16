package com.application.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class AudioDto {
    private String _id;
    private String owner_UUID;
    private String url;
    private String description;
    private long length_in_second;
}
