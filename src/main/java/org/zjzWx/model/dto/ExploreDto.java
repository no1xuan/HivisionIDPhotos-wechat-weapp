package org.zjzWx.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExploreDto {
    private Integer userId;
    //图片分辨率，抠图/六寸排版用到
    private Integer dpi;
    //图片大小，六寸排版用到
    private Integer kb;
    //base64后的图片
    private String processedImage;
    //六寸排版高度
    private Integer height;
    //六寸排版宽度
    private Integer width;
}
