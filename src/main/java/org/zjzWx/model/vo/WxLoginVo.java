package org.zjzWx.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
/*
* 小程序登录用的，微信官方数据+token封装+状态码/错误信息
*
* */
public class WxLoginVo {
    private String token;
    private String sessionKey;
    private String openid;
}
