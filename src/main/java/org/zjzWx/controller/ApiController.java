package org.zjzWx.controller;

import cn.dev33.satoken.stp.StpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.zjzWx.dao.WebSetDao;
import org.zjzWx.entity.WebSet;
import org.zjzWx.model.dto.CreatePhotoDto;
import org.zjzWx.model.vo.PicVo;
import org.zjzWx.model.vo.VideoUnitVo;
import org.zjzWx.service.ApiService;
import org.zjzWx.service.AppSetService;
import org.zjzWx.service.WebSetService;
import org.zjzWx.util.R;

import javax.jws.WebService;


@RestController
@RequestMapping("/api")
public class ApiController {


    @Autowired
    private ApiService apiService;
    @Autowired
    private WebSetService webSetService;
    @Autowired
    private AppSetService appSetService;


    @PostMapping("/getvideoUnit")
    public R getvideoUnit(){
        return R.ok(webSetService.getvideoUnit());
    }

    @PostMapping("/createIdPhoto")
    public R createIdPhoto(@RequestBody CreatePhotoDto createPhotoDto) {
        createPhotoDto.setUserId(Integer.parseInt(StpUtil.getTokenInfo().getLoginId().toString()));
        PicVo idPhoto = apiService.createIdPhoto(createPhotoDto);
        if(null!=idPhoto.getMsg()){
            return R.no(idPhoto.getMsg());
        }
        return R.ok(idPhoto);
    }

    @PostMapping("/createIdHdPhoto")
    public R createIdHdPhoto(@RequestBody CreatePhotoDto createPhotoDto) {
        createPhotoDto.setUserId(Integer.parseInt(StpUtil.getTokenInfo().getLoginId().toString()));
        PicVo idPhoto = apiService.createIdHdPhoto(createPhotoDto);
        if(null!=idPhoto.getMsg()){
            return R.no(idPhoto.getMsg());
        }
        return R.ok(idPhoto);
    }

    @PostMapping("/updateIdPhoto")
    public R updateIdPhoto(@RequestBody CreatePhotoDto createPhotoDto) {
        createPhotoDto.setUserId(Integer.parseInt(StpUtil.getTokenInfo().getLoginId().toString()));
        PicVo idPhoto = apiService.updateIdPhoto(createPhotoDto);
        if(null!=idPhoto.getMsg()){
            return R.no(idPhoto.getMsg());
        }
        return R.ok(idPhoto);
    }



    @PostMapping("/updateUserPhonto")
    public R updateUserPhonto(@RequestBody CreatePhotoDto createPhotoDto){
        Integer userId = Integer.parseInt(StpUtil.getTokenInfo().getLoginId().toString());;
        PicVo picVo = apiService.updateUserPhonto(userId, createPhotoDto.getImage(), createPhotoDto.getPhotoId());
        if(null!=picVo.getMsg()){
            return R.no(picVo.getMsg());
        }
        return R.ok(picVo);
    }



    @PostMapping("/getWebGlow")
    public R getWebGlow(){
        return R.ok(appSetService.getWebGlow());
    }





}
