package org.zjzWx.controller;

import cn.dev33.satoken.stp.StpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.zjzWx.service.OtherApiService;
import org.zjzWx.util.R;

@RestController
@RequestMapping("/otherApi")
public class OtherApiController {

    @Autowired
    private OtherApiService otherApiService;



    @PostMapping("/colourize")
    public R colourize(@RequestBody String image) {
        String colourize = otherApiService.colourize(Integer.parseInt(StpUtil.getTokenInfo().getLoginId().toString()), image);
        if(null==colourize){
            return R.no("图片上色失败，请重试");
        }
        return R.ok(colourize);
    }



}
