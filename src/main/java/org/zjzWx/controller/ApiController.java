package org.zjzWx.controller;

import cn.dev33.satoken.stp.StpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zjzWx.dao.WebSetDao;
import org.zjzWx.entity.Photo;
import org.zjzWx.entity.WebSet;
import org.zjzWx.model.dto.CreatePhotoDto;
import org.zjzWx.model.vo.PicVo;
import org.zjzWx.service.ApiService;
import org.zjzWx.service.PhotoService;
import org.zjzWx.util.R;

@RestController
@RequestMapping("/api")
public class ApiController {


    @Autowired
    private ApiService apiService;
    @Autowired
    private WebSetDao webSetDao;
    @Autowired
    private PhotoService photoService;


    @PostMapping("/getWeb")
    public WebSet getWeb(){
        return webSetDao.getWeb();
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


}
