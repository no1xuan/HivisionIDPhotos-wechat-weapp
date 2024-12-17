package org.zjzWx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.zjzWx.entity.WebSet;
import org.zjzWx.model.vo.VideoUnitVo;

public interface WebSetService extends IService<WebSet> {

    //查询广告位ID和下载高清照是否开启广告
    VideoUnitVo getvideoUnit();

}
