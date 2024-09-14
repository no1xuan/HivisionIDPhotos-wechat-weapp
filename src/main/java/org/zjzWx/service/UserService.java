package org.zjzWx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.zjzWx.entity.User;
import org.zjzWx.model.vo.WxLoginVo;

public interface UserService extends IService<User> {

//微信登录
 WxLoginVo wxlogin(String code);
}
