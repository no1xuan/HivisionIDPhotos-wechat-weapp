package org.zjzWx.service.impl;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.zjzWx.dao.UserDao;
import org.zjzWx.entity.User;
import org.zjzWx.entity.WebSet;
import org.zjzWx.model.vo.WxLoginVo;
import org.zjzWx.service.UserService;
import org.springframework.stereotype.Service;
import org.zjzWx.service.WebSetService;
import org.zjzWx.util.HttpClient;

import java.io.IOException;
import java.util.Date;


@Service
public class UserServiceImpl extends ServiceImpl<UserDao,User> implements UserService {

    @Autowired
    private WebSetService webSetService;




    @Override
    public WxLoginVo wxlogin(String code) {
        WxLoginVo wxlogin = null;
        try {
            WebSet webSet = webSetService.getById(1);
            //发起登录请求
            String url = "https://api.weixin.qq.com/sns/jscode2session?appid="+webSet.getAppId()
                    +"&secret="+webSet.getAppSecret()+"&js_code=" + code + "&grant_type=authorization_code";
            HttpClient http = new HttpClient(url);
            http.setHttps(true);
            http.post();
            String content = http.getContent();
            //格式化微信官方返回
            JSONObject jsonopenid = JSONObject.parseObject(content);
            String openid = jsonopenid.getString("openid");

            QueryWrapper<User> qw = new QueryWrapper<>();
            qw.eq("openid",openid);
            User user = baseMapper.selectOne(qw);
            if(null==user){
                user = new User(null,openid,null,null,new Date());
                baseMapper.insert(user);
            }
            StpUtil.login(user.getId());
            //封装信息返回前端
            wxlogin = new WxLoginVo();
            wxlogin.setOpenid(openid);
            wxlogin.setToken(StpUtil.getTokenInfo().getTokenValue());


        } catch (IOException e) {
            e.printStackTrace();
        }
        return wxlogin;
    }
}
