package org.zjzWx.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PicVo {
    //原始图片 (目前不作为原始图片使用，存放的高清抠图)
    private String oImg;
    //高清抠图透明图片
    private String kImg;
    //已经上色图片
    private String cImg;
    //错误消息
    private String msg;
    //附加参数
    private Integer id2;
    //图片地址
    private String picUrl;
}
