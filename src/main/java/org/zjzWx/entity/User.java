package org.zjzWx.entity;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user")
public class User {
/**
     * 用户表id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
/**
     * openid
     */
    private String openid;
/**
     * 用户名字
     */
    private String nickname;
/**
     * 用户头像
     */
    private String avatarUrl;
    /**
     * 1正常，2禁止登录
     */
    private Integer status;

/**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;



}
