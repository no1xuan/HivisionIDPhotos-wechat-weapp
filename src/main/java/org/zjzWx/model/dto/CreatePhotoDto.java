package org.zjzWx.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePhotoDto {
    private String image;
    private Integer height;
    private Integer width;
    private String colors;

    //规格id
    private Integer itemId;
    //记录id
    private Integer photoId;

    private Integer userId;
    private Integer type;
}
