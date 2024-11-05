package org.zjzWx.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.zjzWx.dao.UserDao;
import org.zjzWx.entity.User;
import org.zjzWx.entity.WebSet;
import org.zjzWx.model.vo.WxLoginVo;
import org.zjzWx.service.UserService;
import org.springframework.stereotype.Service;
import org.zjzWx.service.WebSetService;
import org.zjzWx.util.HttpClient;
import org.zjzWx.util.PicUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
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
            if(null==openid){  //高风险用户会存在openid没有的情况/数据库配置错误/安全域名没有添加也会出现
                wxlogin.setMsg(jsonopenid.toString());
                return wxlogin;
            }

            QueryWrapper<User> qw = new QueryWrapper<>();
            qw.eq("openid",openid);
            User user = baseMapper.selectOne(qw);
            if(null==user){
                user = new User(null,openid,null,null,new Date());
                baseMapper.insert(user);
            }
            StpUtil.login(user.getId());
            //封装信息返回前端
            wxlogin.setOpenid(openid);
            wxlogin.setToken(StpUtil.getTokenInfo().getTokenValue());


        } catch (IOException e) {
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
            // 不按照日期，全部扔一个里面，方便排查是否被当图床，是否被上传黄色
            String folderName = "avatar";
            File uploadFolder = new File(directory, folderName);
            if (!uploadFolder.exists()) {
                uploadFolder.mkdirs();
            }

            // 生成新的文件名
            String filename = PicUtil.generateUniqueFilename(originalFilename, file);
            Path filePath = uploadFolder.toPath().resolve(filename);


            // 保存文件
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            //nginx帮助
            String imagePath = picDomain + folderName + "/" + filename;
            mp.put("type",1);
            mp.put("msg",imagePath);
            return mp;
        } catch (IOException e) {
            e.printStackTrace();
            mp.put("type",0);
            mp.put("msg","头像保存失败，请重试");
            return mp;
        }
    }
}
