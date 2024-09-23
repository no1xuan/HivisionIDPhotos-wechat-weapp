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
@TableName("photo")
public class Photo {
/**
     * 图片表
     */
@TableId(type = IdType.AUTO)
    private Integer id;
/**
     * 用户id
     */
    private Integer userId;
/**
     * 规格名字
     */
    private String name;
/**
     * 原图
     */
    private String oImg;

    /**
     * 保存图
     */
    private String nImg;
/**
     * 尺寸
     */
    private String size;
/**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;



}
