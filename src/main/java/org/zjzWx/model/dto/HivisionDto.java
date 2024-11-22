package org.zjzWx.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HivisionDto {
    //是否制作成功
    private boolean status;
    //不高清抠图透明图片
    private String imageBase64Standard;
    //高清抠图透明图片
    private String imageBase64Hd;
    //切换颜色后的上色图片 或 探索功能里面的抠图/排版照结果图
    private String imageBase64;
}
