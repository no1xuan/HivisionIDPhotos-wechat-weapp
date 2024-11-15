package org.zjzWx.service;

import org.zjzWx.model.dto.ExploreDto;

//其它乱七八糟的小功能的APi
public interface OtherApiService {

    //探索页面数据
    ExploreDto exploreDtoCount();

    //黑白图片上色
    String colourize(Integer userId,String img);

    //通用抠图
    String matting(Integer userId,String img,Integer dpi);

}
