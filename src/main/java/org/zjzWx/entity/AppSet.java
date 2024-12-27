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
@TableName("app_set")
public class AppSet {
/**
     * 探索中心设置表
     */
@TableId(type = IdType.AUTO)
    private Integer id;
/**
     * 类型：1鉴黄，2美颜，3智能证件照，4六寸排版照，5老照片上色，6智能抠图，7图片编辑，8新海诚动漫风
     */
    private Integer type;
/**
     * 名字
     */
    private String name;
/**
     * 0关闭，1开启，2次数限制（status==2时type=1和2没用 | type==3时 0代表关闭下载高清证件照广告，1代表开启广告下载高清证件照广告，2没用）
     */
    private Integer status;
/**
     * 用户每天免费次数（type=1，2，3没用）
     */
    private Integer counts;



}
