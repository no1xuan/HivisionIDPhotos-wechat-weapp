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
    private Integer dpi;

    //规格id
    private Integer itemId;
    //记录id
    private Integer photoId;
    //换色方式  0纯色 1上下渐变 2中心渐变
    private Integer render;
    //图片kb
    private Integer kb;
    //美颜开关
    private Integer isBeautyOn;

    private Integer userId;
    private Integer type;
}
