package org.zjzWx.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Page;
import org.zjzWx.entity.*;
import org.zjzWx.model.vo.AdminIndexVo;
import org.zjzWx.model.vo.AdminLoginVo;

public interface AdminService extends IService<Admin> {

    //登录二维码生成
    AdminLoginVo login();

    //检查登录
    String checkLogin(String code);

    //登录成功code1=微信code，code2=系统code
    String okLogin(String code1,String code2);

    //管理员首页数据
    AdminIndexVo adminIndex();

    //规格列表
    IPage<Item> getItemPage(int pageNum, int pageSize, String name);

    //用户自定义分页
    IPage<Custom> getCustomPage(int pageNum, int pageSize, String name);

    //保存列表
    IPage<Photo> getPhotoPage(int pageNum, int pageSize, String name);

    //行为记录
    IPage<PhotoRecord> getPhotoRecordPage(int pageNum, int pageSize, Integer userId);

    //用户列表
    IPage<User> getUserPage(int pageNum, int pageSize, String name);

    //读取系统设置
    WebSet getWebSet();

    //修改系统设置
    void updateWebSet(WebSet webSet);
}
