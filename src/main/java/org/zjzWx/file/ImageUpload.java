package org.zjzWx.file;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.zjzWx.entity.AppSet;
import org.zjzWx.entity.PhotoRecord;
import org.zjzWx.entity.WebSet;
import org.zjzWx.service.AppSetService;
import org.zjzWx.service.PhotoRecordService;
import org.zjzWx.service.UploadService;
import org.zjzWx.service.WebSetService;
import org.zjzWx.util.R;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

@RestController
public class ImageUpload {

    @Autowired
    private UploadService uploadService;
    @Autowired
    private PhotoRecordService photoRecordService;
    @Autowired
    private AppSetService appSetService;

    //前端所有图片上传都会经过这个接口检查，如通过返回base64，不通过返回错误信息
    @PostMapping("/upload")
    public R uploadImage(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return R.no("图片不能为空");
        }

        // 检查文件类型
        String originalFilename = file.getOriginalFilename();
        if (!StringUtils.hasText(originalFilename) || (!originalFilename.toLowerCase().endsWith(".png")
                && !originalFilename.toLowerCase().endsWith(".jpg")) && !originalFilename.toLowerCase().endsWith(".jpeg")) {
            // 文件类型不合法
            return R.no("图片类型不合法，仅支持jpg/png/jpeg的图片");
        }

        // 检查文件大小，因为现在的手机，一拍照就10多M
        if (file.getSize() > 15 * 1024 * 1024) {
            return R.no("图片大小不能超过15M");
        }

        QueryWrapper<AppSet> qwapp = new QueryWrapper<>();
        qwapp.eq("type",1);
        AppSet appSet = appSetService.getOne(qwapp);
        //如果开启鉴黄
        if(appSet.getStatus()==1){
            String s = uploadService.checkNsfw(file);
            if(s!=null){
                return R.no(s);
            }
        }

        PhotoRecord photoRecord = new PhotoRecord();
        photoRecord.setType(10);
        photoRecord.setName("上传图片");
        photoRecord.setUserId(Integer.parseInt(StpUtil.getTokenInfo().getLoginId().toString()));
        photoRecord.setCreateTime(new Date());
        photoRecordService.save(photoRecord);


        return uploadService.uploadPhoto(file,originalFilename);
    }


}
