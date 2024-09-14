package org.zjzWx.entity;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
    private Date createTime;



}
