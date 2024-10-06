package org.zjzWx.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.zjzWx.entity.Photo;
import org.zjzWx.entity.PhotoRecord;
import org.zjzWx.entity.User;
import org.zjzWx.entity.WebSet;
import org.zjzWx.model.vo.AdminIndexVo;
import org.zjzWx.model.vo.AdminLoginVo;
import org.zjzWx.service.AdminService;
import org.zjzWx.util.PicUtil;
import org.zjzWx.util.R;


@CrossOrigin
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;


    //获取二维码
    @GetMapping("/login")
    public R login() {
        AdminLoginVo login = adminService.login();
        if(null==login){
            return R.no();
        }
        return R.ok(login);
    }


    //轮询检查登录拿token
    @GetMapping("/checkLogin")
    public R checkLogin(String code) {
        if(null==code || code.trim().isEmpty()){
           return R.no();
        }
        String msg = adminService.checkLogin(code);
        if(null==msg){
            return R.no();
        }
        return R.ok(msg);
    }

    //小程序请求修改登录
    @GetMapping("/okLogin")
    public R okLogin(String code1,String code2) {
        if(null==code1 || code1.trim().isEmpty() || null==code2 || code2.trim().isEmpty()){
            return R.no();
        }
        String msg = adminService.okLogin(code1, code2);
        if(null==msg){
            return R.no();
        }
        return R.ok(null);
    }


    //管理员首页数据
    @PostMapping("/adminIndex")
    public R adminIndex(){
        int id = Integer.parseInt(StpUtil.getTokenInfo().getLoginId().toString());
        if(id!=1){
            return R.no("非法请求");
        }
        return R.ok(adminService.adminIndex());
    }



    //管理员规格列表
    @PostMapping("/getItemPage")
    public R getItemPage(int pageNum, int pageSize, String name){
        int id = Integer.parseInt(StpUtil.getTokenInfo().getLoginId().toString());
        if(id!=1){
            return R.no("非法请求");
        }
        return R.ok(adminService.getItemPage(pageNum, pageSize, name));
    }


    //管理员用户定制规格列表
    @PostMapping("/getCustomPage")
    public R getCustomPage(int pageNum, int pageSize, String name){
        int id = Integer.parseInt(StpUtil.getTokenInfo().getLoginId().toString());
        if(id!=1){
            return R.no("非法请求");
        }
        return R.ok(adminService.getCustomPage(pageNum, pageSize, name));
    }


    //保存列表
    @PostMapping("/getPhotoPage")
    public R getPhotoPage(int pageNum, int pageSize, String name){
        int id = Integer.parseInt(StpUtil.getTokenInfo().getLoginId().toString());
        if(id!=1){
            return R.no("非法请求");
        }
        return R.ok(adminService.getPhotoPage(pageNum,pageSize,name));

    }

    //行为记录
    @PostMapping("/getPhotoRecordPage")
    public R getPhotoRecordPage(int pageNum, int pageSize, Integer userId){
        int id = Integer.parseInt(StpUtil.getTokenInfo().getLoginId().toString());
        if(id!=1){
            return R.no("非法请求");
        }
        return R.ok(adminService.getPhotoRecordPage(pageNum,pageSize,userId));
    }

    //用户列表
    @PostMapping("/getUserPage")
    public R getUserPage(int pageNum, int pageSize, String name){
        int id = Integer.parseInt(StpUtil.getTokenInfo().getLoginId().toString());
        if(id!=1){
            return R.no("非法请求");
        }
        return R.ok(adminService.getUserPage(pageNum,pageSize,name));

    }

    //读取系统设置
    @PostMapping("/getWebSet")
    public R getWebSet(){
        int id = Integer.parseInt(StpUtil.getTokenInfo().getLoginId().toString());
        if(id!=1){
            return R.no("非法请求");
        }
        return R.ok(adminService.getWebSet());

    }

    //修改系统设置
    @PostMapping("/updateWebSet")
    public R updateWebSet(@RequestBody WebSet webSet){
        int id = Integer.parseInt(StpUtil.getTokenInfo().getLoginId().toString());
        if(id!=1){
            return R.no("非法请求");
        }
        adminService.updateWebSet(webSet);
        return R.ok(null);

    }


}
