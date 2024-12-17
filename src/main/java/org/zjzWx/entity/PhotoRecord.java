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
@TableName("photo_record")
public class PhotoRecord {
/**
     * 用户行为记录
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 类型：
     * 0旧数据，1生成证件照，2生成高清证件照，3换背景，4下载证件照，5老照片上色
     * 6智能抠图，7六寸排版照，8动漫风照，9照片清晰增强，10上传图片
     */
    private Integer type;
/**
     * 名字
     */
    private String name;
/**
     * 用户id
     */
    private Integer userId;
/**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;



}
