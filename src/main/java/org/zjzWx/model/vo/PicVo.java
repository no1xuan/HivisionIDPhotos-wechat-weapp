package org.zjzWx.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//这个实体类遇到个很蛋疼的问题
//oImg到前端变成了oimg，一开始我没发现，后面我没看返回值就直接赋值到前端，然后就直接报错了
//为了统一，防止后人产生误解，所以我把本实体类也改成小写了（虽然不规范）
//如果你就喜欢大写，可以看这个文章来解决：https://cloud.tencent.com/developer/article/2147389

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PicVo {
    //原始图片
    private String oimg;
    //高清抠图透明图片
    private String kimg;
    //已经上色图片
    private String cimg;
    //错误消息
    private String msg;
    //附加参数
    private Integer id2;
    //图片地址
    private String picUrl;
    //图片分辨率
    private Integer dpi;
}
