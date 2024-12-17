package org.zjzWx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.zjzWx.dao.WebSetDao;
import org.zjzWx.entity.AppSet;
import org.zjzWx.entity.WebSet;
import org.zjzWx.model.vo.VideoUnitVo;
import org.zjzWx.service.AppSetService;
import org.zjzWx.service.WebSetService;
import org.springframework.stereotype.Service;


@Service
public class WebSetServiceImpl extends ServiceImpl<WebSetDao,WebSet> implements WebSetService {

    @Autowired
    private AppSetService appSetService;

    @Override
    public VideoUnitVo getvideoUnit() {
        VideoUnitVo videoUnitVo = new VideoUnitVo();
        videoUnitVo.setVideoUnitId(baseMapper.selectById(1).getVideoUnitId());
        QueryWrapper<AppSet> qw = new QueryWrapper<>();
        qw.eq("type",3);
        videoUnitVo.setDownloadHd(appSetService.getOne(qw).getStatus());
        return videoUnitVo;
    }
}
