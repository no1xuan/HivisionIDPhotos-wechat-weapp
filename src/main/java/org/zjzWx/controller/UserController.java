package org.zjzWx.controller;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.web.bind.annotation.*;
import org.zjzWx.entity.User;
import org.zjzWx.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.zjzWx.util.R;

import java.util.Date;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;


    //登录
    @GetMapping("/login")
    public R login(String code){
        if(code==null){
            return R.no(null);
        }
       return R.ok(userService.wxlogin(code));
    }
}
