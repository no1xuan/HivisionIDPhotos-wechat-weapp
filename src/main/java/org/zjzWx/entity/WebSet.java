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
@TableName("web_set")
public class WebSet {
/**
     * 系统设置表
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
/**
     * 小程序appid
     */
    private String appId;
/**
     * 小程序AppSecret
     */
    private String appSecret;
/**
     * 广告位id
     */
    private String videoUnitId;


}
