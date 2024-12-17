package org.zjzWx.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExploreIndexAdminDto {
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
    //清晰度数量
    private long imageDefinitionEnhanceCount;
    //上传图片数量
    private long imageuploadCount;
    //总计
    private long imageCount;
}
