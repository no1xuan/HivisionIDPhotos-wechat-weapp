package org.zjzWx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.zjzWx.App;
import org.zjzWx.dao.ExploreSetDao;
import org.zjzWx.entity.AppSet;
import org.zjzWx.service.AppSetService;
import org.springframework.stereotype.Service;


@Service
public class AppSetServiceImpl extends ServiceImpl<ExploreSetDao, AppSet> implements AppSetService {


    @Override
    public Integer getWebGlow() {
        QueryWrapper<AppSet> qw = new QueryWrapper<>();
        qw.eq("type",2);
        AppSet appSet = baseMapper.selectOne(qw);
        if(null!=appSet){
            return appSet.getStatus();
        }
        return 0;
    }
}
