package org.zjzWx.controller;

import cn.dev33.satoken.stp.StpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.zjzWx.entity.*;
import org.zjzWx.model.dto.ExploreIndexAdminDto;
import org.zjzWx.model.vo.AdminLoginVo;
import org.zjzWx.service.AdminService;
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
            return R.no("无效的请求登录");
        }
        String msg = adminService.okLogin(code1, code2);
        if(null!=msg){
            return R.no(msg);
        }
        return R.ok(null);
    }


    //首页数据
    @PostMapping("/adminIndex")
    public R adminIndex(){
        int id = Integer.parseInt(StpUtil.getTokenInfo().getLoginId().toString());
        if(id!=1){
            return R.no("非法请求");
        }
        return R.ok(adminService.adminIndex());
    }



    //规格列表
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


    //读取美颜设置
    @PostMapping("/getWebGlow")
    public R getWebGlow(){
        int id = Integer.parseInt(StpUtil.getTokenInfo().getLoginId().toString());
        if(id!=1){
            return R.no("非法请求");
        }
        return R.ok(adminService.getWebGlow());

    }

    //修改美颜设置
    @PostMapping("/updateWebGlow")
    public R updateWebGlow(@RequestBody WebGlow webGlow){
        int id = Integer.parseInt(StpUtil.getTokenInfo().getLoginId().toString());
        if(id!=1){
            return R.no("非法请求");
        }
        adminService.updateWebGlow(webGlow);
        return R.ok(null);

    }

    //读取探索中心设置
    @PostMapping("/getExploreSet")
    public R getExploreSet(){
        int id = Integer.parseInt(StpUtil.getTokenInfo().getLoginId().toString());
        if(id!=1){
            return R.no("非法请求");
        }
        return R.ok(adminService.getExploreSet());

    }

    //修改探索中心设置
    @PostMapping("/updateExploreSet")
    public R updateExploreSet(@RequestBody AppSet appSet){
        int id = Integer.parseInt(StpUtil.getTokenInfo().getLoginId().toString());
        if(id!=1){
            return R.no("非法请求");
        }
        adminService.updateExploreSet(appSet);
        return R.ok(null);
    }

    //操作用户状态
    @PostMapping("/updateUserStatus")
    public R updateUserStatus(Integer userId,Integer type){
        int id = Integer.parseInt(StpUtil.getTokenInfo().getLoginId().toString());
        if(id!=1){
            return R.no("非法请求");
        }
        if(type<1 || type>2){
            return R.no("非法请求");
        }
        adminService.updateUserStatus(userId,type);
        return R.ok(null);
    }

    //使用量统计
    @PostMapping("/exploreIndexAdmin")
    public R exploreIndexAdmin(){
        int id = Integer.parseInt(StpUtil.getTokenInfo().getLoginId().toString());
        if(id!=1){
            return R.no("非法请求");
        }
        return R.ok(adminService.exploreIndexAdmin());
    }



}
