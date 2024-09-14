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
    //初始化白底证件照图片
    private String imageBase64Standard;
    //高清抠图透明图片
    private String imageBase64Hd;
    //切换颜色上色后图片
    private String imageBase64;
}
