package org.zjzWx.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Admin {
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 0等待登录，1登录成功
     */
    private Integer status;
    /**
     * 识别码
     */
    private long code;
}
