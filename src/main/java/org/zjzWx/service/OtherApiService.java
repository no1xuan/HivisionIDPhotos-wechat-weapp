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

    //生成动漫风滤镜（新海诚画风）
    String cartoon(ExploreDto exploreDto);

    //返回用户剩余次数,type对应explore_set表，type2对应photo_record
    long checkTheFreeQuota(Integer type,Integer type2,Integer userId);
}
