package org.zjzWx.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExploreIndexDto {
    //证件照数量
    private long zjzCount;
    //六寸排版照数量
    private long generateLayoutCount;
    //老照片数量
    private long colourizeCount;
    //抠图数量
    private long mattingCount;
    //动漫数量
    private long cartoonCount;
    //图片编辑数量
    private long editImageCount;
}
