package org.zjzWx.controller;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.web.bind.annotation.*;
import org.zjzWx.entity.Custom;
import org.zjzWx.entity.Photo;
import org.zjzWx.service.CustomService;
import org.zjzWx.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.zjzWx.service.PhotoService;
import org.zjzWx.util.R;

import java.util.Date;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/item")
public class ItemController {


    @Autowired
    private ItemService itemService;
    @Autowired
    private CustomService customService;
    @Autowired
    private PhotoService photoService;


    //保存用户自定义
    @PostMapping("/saveCustom")
    public R saveCustom(@RequestBody Custom custom){
        custom.setId(null);
        custom.setUserId(Integer.parseInt(StpUtil.getTokenInfo().getLoginId().toString()));
        Random random = new Random();
        custom.setIcon(random.nextInt(6) + 1);
        custom.setWidthMm(custom.getWidthMm());
        custom.setHeightMm(custom.getHeightMm());
        custom.setCreateTime(new Date());
        customService.save(custom);
        return R.ok(custom);
    }

    //证件列表
    @GetMapping("/itemList")
   public R itemList(int pageNum, int pageSize, int type,String name){
        String userId = "0";
        if(type==4){
            userId = StpUtil.getTokenInfo().getLoginId().toString();
        }
        return R.ok(itemService.itemList(pageNum, pageSize, type,userId,name));
    }


    //用户作品列表
    @GetMapping("/photoList")
    public R photoList(int pageNum, int pageSize){
        return R.ok(photoService.photoList(pageNum, pageSize, StpUtil.getTokenInfo().getLoginId().toString()));
    }

    //删除作品
    @GetMapping("/deletePhotoId")
    public R deletePhotoId(int id){
        photoService.deletePhotoId(id,StpUtil.getTokenInfo().getLoginId().toString());
        return R.ok(null);
    }

}
