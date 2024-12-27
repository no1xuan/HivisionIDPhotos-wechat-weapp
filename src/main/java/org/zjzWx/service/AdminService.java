package org.zjzWx.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.zjzWx.entity.*;
import org.zjzWx.model.dto.ExploreIndexAdminDto;
import org.zjzWx.model.dto.ExploreIndexDto;
import org.zjzWx.model.vo.AdminIndexVo;
import org.zjzWx.model.vo.AdminLoginVo;

import java.util.List;

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
    IPage<Custom> getCustomPage(int pageNum, int pageSize, int userId);

    //保存列表
    IPage<Photo> getPhotoPage(int pageNum, int pageSize, int userId,String name);

    //行为记录
    IPage<PhotoRecord> getPhotoRecordPage(int pageNum, int pageSize,int userId);

    //用户列表
    IPage<User> getUserPage(int pageNum, int pageSize,int userId,String name);

    //读取系统设置
    WebSet getWebSet();

    //修改系统设置
    void updateWebSet(WebSet webSet);

    //读取美颜设置
    WebGlow getWebGlow();

    //修改美颜设置
    void updateWebGlow(WebGlow webGlow);

    //读取探索中心设置
    List<AppSet> getExploreSet();

    //修改探索中心设置
    void updateExploreSet(AppSet appSet);

    //用户列表面板：type=1踢掉登录状态，2删除定制记录，3删除保存记录，4删除行为记录，5禁止登录并踢掉登录，6恢复登录
    String updateUserStatus(Integer userId,Integer type);

    //使用量统计
    ExploreIndexAdminDto exploreIndexAdmin();


}
