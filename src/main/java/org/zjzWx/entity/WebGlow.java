package org.zjzWx.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("web_glow")
public class WebGlow {
/**
     * 美颜参数配置表
     */
@TableId(type = IdType.AUTO)
    private Integer id;
/**
     * 亮度调整强度,最大25
     */
    private Integer brightnessStrength;
/**
     * 对比度调整强度，最大50
     */
    private Integer contrastStrength;
/**
     * 锐化调整强度，最大50
     */
    private Integer sharpenStrength;
/**
     * 饱和度调整强度，最大5
     */
    private Integer saturationStrength;



}
