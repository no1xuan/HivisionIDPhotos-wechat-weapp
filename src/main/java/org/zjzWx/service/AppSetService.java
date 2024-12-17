package org.zjzWx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.zjzWx.entity.AppSet;

public interface AppSetService extends IService<AppSet> {

    //前端获取管理员是否开启美颜功能
    Integer getWebGlow();

}
