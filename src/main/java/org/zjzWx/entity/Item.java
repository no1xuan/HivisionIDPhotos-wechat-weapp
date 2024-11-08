package org.zjzWx.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("item")
public class Item {

    @TableId(type = IdType.AUTO)
    private Integer id;
/**
     * 名称
     */
    private String name;
/**
     * 像素-宽
     */
    private Integer widthPx;
/**
     * 像素-高
     */
    private Integer heightPx;
/**
     * 尺寸-宽
     */
    private Integer widthMm;
/**
     * 尺寸-高
     */
    private Integer heightMm;
    /**
     * 图标，1-6
     */
    private Integer icon;

/**
     * 排序
     */
    private Integer sort;
/**
     * 1=常用寸照，2=各类签证，3=各类证件
     */
    private Integer category;

    private Integer dpi;



}
