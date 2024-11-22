package org.zjzWx.service;

import org.zjzWx.model.dto.ExploreDto;
import org.zjzWx.model.dto.ExploreIndexDto;

//主要放探索功能里面的APi
public interface OtherApiService {

    //页面统计数据
    ExploreIndexDto exploreDtoCount();

    //黑白图片上色
    String colourize(ExploreDto exploreDto);

    //智能抠图
    String matting(ExploreDto exploreDto);

    //生成六寸排版照
    String generateLayoutPhotos(ExploreDto exploreDto);

}
