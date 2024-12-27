package org.zjzWx.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.zjzWx.dao.UserDao;
import org.zjzWx.entity.User;
import org.zjzWx.entity.WebSet;
import org.zjzWx.model.vo.WxLoginVo;
import org.zjzWx.service.UserService;
import org.springframework.stereotype.Service;
import org.zjzWx.service.WebSetService;
import org.zjzWx.util.PicUtil;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Service
public class UserServiceImpl extends ServiceImpl<UserDao,User> implements UserService {

    @Autowired
    private WebSetService webSetService;
    @Value("${webset.directory}")
    private String directory;

    @Value("${webset.picDomain}")
    private String picDomain;




    @Override
    public WxLoginVo wxlogin(String code) {
        WxLoginVo wxlogin = new WxLoginVo();
        try {
            WebSet webSet = webSetService.getById(1);
            String url = "https://api.weixin.qq.com/sns/jscode2session?appid="+webSet.getAppId()
                    +"&secret="+webSet.getAppSecret()+"&js_code=" + code + "&grant_type=authorization_code";

            //发起请求
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            JSONObject jsonopenid = JSONObject.parseObject(response.getBody());
            if(null==jsonopenid){
                wxlogin.setMsg("与微信通讯失败，请重试");
                return wxlogin;
            }

            String openid = jsonopenid.getString("openid");
            // 高风险的微信用户/数据库配置错误/安全域名没有添加会存在openid没有的情况
            if (null==openid) {
                wxlogin.setMsg(jsonopenid.toString());
                return wxlogin;
            }

            //获取openid成功
            QueryWrapper<User> qw = new QueryWrapper<>();
            qw.eq("openid",openid);
            User user = baseMapper.selectOne(qw);
            if(null!=user && user.getStatus()==2){
                wxlogin.setMsg("您已申请注销，无法登录，可联系客服恢复");
                return wxlogin;
            }

            if(null==user){
                user = new User(null,openid,null,null,1,new Date());
                baseMapper.insert(user);
            }
            StpUtil.login(user.getId());
            //封装信息返回前端
            wxlogin.setOpenid(openid);
            wxlogin.setToken(StpUtil.getTokenInfo().getTokenValue());


        } catch (Exception e) {
            wxlogin.setMsg("代码报错");
            e.printStackTrace();
        }
        return wxlogin;
    }

    @Override
    public String updateUserInfo(MultipartFile file,String nickname,Integer userId) {
        User user = new User();
        user.setId(userId);
        if(null!=file){
            Map<String, Object> mp = this.updateAvatar(file);
            if((int)mp.get("type")==0){
                return (String) mp.get("msg");
            }
            user.setAvatarUrl((String) mp.get("msg"));
        }
        if(null!=nickname && !nickname.trim().isEmpty()){
            user.setNickname(nickname);
        }
        if(null!=user.getAvatarUrl() || null!=user.getNickname()){
            baseMapper.updateById(user);
        }
        return null;
    }




    //上传用户头像
    private Map<String,Object> updateAvatar(MultipartFile file){
        Map<String,Object> mp = new HashMap<>();
        //无需对头像和昵称鉴黄，根据文档，腾讯帮你鉴黄过了，但是如果被抓接口，就完蛋了，后期有空再对所有请求参数进行加密
        // 检查文件类型
        String originalFilename = file.getOriginalFilename();
        if (!StringUtils.hasText(originalFilename) || (!originalFilename.toLowerCase().endsWith(".png")
                && !originalFilename.toLowerCase().endsWith(".jpg")) && !originalFilename.toLowerCase().endsWith(".jpeg")) {
            mp.put("type",0);
            mp.put("msg","图片类型不合法，仅支持jpg/png/jpeg的图片");
            return mp;
        }
        //防止被当图床，还是要限制下，但是不告诉前端限制了多少，无需解释
        if (file.getSize() > 1 * 1024 * 1024) {
            mp.put("type",0);
            mp.put("msg","头像太大啦");
            return mp;
        }

        try {
            // 进行存储图片
            // 不按照日期，全部扔一个avatar文件夹里面，方便排查是否被当图床，是否被上传黄色
            String filename = PicUtil.filesCopy("avatar", directory, originalFilename, file);
            String imagePath = picDomain + "avatar" + "/" + filename;
            mp.put("type",1);
            mp.put("msg",imagePath);
            return mp;
        } catch (Exception e) {
            e.printStackTrace();
            mp.put("type",0);
            mp.put("msg","头像保存失败，请重试");
            return mp;
        }
    }
}
