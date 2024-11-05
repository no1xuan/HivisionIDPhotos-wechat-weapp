package org.zjzWx.controller;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.zjzWx.entity.User;
import org.zjzWx.model.vo.WxLoginVo;
import org.zjzWx.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.zjzWx.util.R;


@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;


    //登录
    @GetMapping("/login")
    public R login(String code){
        if(null==code){
            return R.no();
        }
        WxLoginVo wxlogin = userService.wxlogin(code);
        if(null!=wxlogin.getOpenid()){
            return R.ok(wxlogin);
        }
        return R.no(wxlogin.getMsg());
    }

    //获取用户信息
    @GetMapping("/userInfo")
    public R userInfo(){
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.select("nickname","avatar_url","create_time");
        qw.eq("id",StpUtil.getTokenInfo().getLoginId());
        User user = userService.getOne(qw);
        if(null==user){
            return R.no();
        }
        return R.ok(user);
    }

    //保存用户信息
    @PostMapping("/updateUserInfo")
    public R updateUserInfo(@RequestParam(name = "file", required = false) MultipartFile file,
                            @RequestParam(name = "nickname", required = false) String nickname){
        if(null==file){
            if(null!=nickname && nickname.length()>20){
                return R.no("名字太长啦~");
            }
        }
        String msg = userService.updateUserInfo(file,nickname,Integer.parseInt(StpUtil.getTokenInfo().getLoginId().toString()));
        if(null!=msg){
            return R.no(msg);
        }
        return R.ok(null);
    }











}
