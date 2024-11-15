package org.zjzWx.controller;

import cn.dev33.satoken.stp.StpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.zjzWx.model.dto.ColourizeDto;
import org.zjzWx.service.OtherApiService;
import org.zjzWx.util.R;

@RestController
@RequestMapping("/otherApi")
public class OtherApiController {

    @Autowired
    private OtherApiService otherApiService;


    @GetMapping("/exploreCount")
    public R exploreCount(){
        return R.ok(otherApiService.exploreDtoCount());
    }


    @PostMapping("/colourize")
    public R colourize(@RequestBody ColourizeDto colourizeDto) {
        String colourize = otherApiService.colourize(Integer.parseInt(StpUtil.getTokenInfo().getLoginId().toString()), colourizeDto.getProcessedImage());
        if(null==colourize){
            return R.no("图片上色失败，请重试");
        }
        return R.ok(colourize);
    }

    @PostMapping("/matting")
    public R matting(@RequestBody ColourizeDto colourizeDto) {
        String colourize = otherApiService.matting(Integer.parseInt(StpUtil.getTokenInfo().getLoginId().toString()),colourizeDto.getProcessedImage(),300);
        if(null==colourize){
            return R.no("图片抠图失败，请重试");
        }
        return R.ok(colourize);
    }

}
