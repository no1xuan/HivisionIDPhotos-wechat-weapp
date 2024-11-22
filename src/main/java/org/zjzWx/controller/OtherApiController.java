package org.zjzWx.controller;

import cn.dev33.satoken.stp.StpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.zjzWx.model.dto.ColourizeDto;
import org.zjzWx.model.dto.ExploreDto;
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
    public R colourize(@RequestBody ExploreDto exploreDto) {
        exploreDto.setUserId(Integer.parseInt(StpUtil.getTokenInfo().getLoginId().toString()));
        String colourize = otherApiService.colourize(exploreDto);
        if(null==colourize){
            return R.no("图片上色失败，请重试");
        }
        return R.ok(colourize);
    }

    @PostMapping("/matting")
    public R matting(@RequestBody ExploreDto exploreDto) {
        exploreDto.setUserId(Integer.parseInt(StpUtil.getTokenInfo().getLoginId().toString()));
        String matting = otherApiService.matting(exploreDto);
        if(null==matting){
            return R.no("图片抠图失败，请重试");
        }
        return R.ok(matting);
    }


    @PostMapping("/generateLayoutPhotos")
    public R generateLayoutPhotos(@RequestBody ExploreDto exploreDto) {
        exploreDto.setUserId(Integer.parseInt(StpUtil.getTokenInfo().getLoginId().toString()));
        String generateLayoutPhotos = otherApiService.generateLayoutPhotos(exploreDto);
        if(null==generateLayoutPhotos){
            return R.no("图片制作失败，请重试");
        }
        return R.ok(generateLayoutPhotos);
    }

}
