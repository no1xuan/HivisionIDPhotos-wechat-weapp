package org.zjzWx.service.impl;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.zjzWx.entity.*;
import org.zjzWx.model.dto.CreatePhotoDto;
import org.zjzWx.model.dto.HivisionDto;
import org.zjzWx.model.vo.PicVo;
import org.zjzWx.service.*;
import org.zjzWx.util.PicUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class ApiServiceImpl implements ApiService {

    @Value("${webset.zjzDomain}")
    private String zjzDomain;
    @Value("${webset.directory}")
    private String directory;
    @Value("${webset.picDomain}")
    private String picDomain;

    @Value("${modelset.humanMattingModel}")
    private String humanMattingModel;
    @Value("${modelset.faceDetectModel}")
    private String faceDetectModel;


    @Autowired
    private CustomService customService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private PhotoService photoService;
    @Autowired
    private PhotoRecordService photoRecordService;





    @Override
    public PicVo createIdPhoto(CreatePhotoDto createPhotoDto) {
        PicVo picVo = new PicVo();
        Item item = null;
        if(createPhotoDto.getType()==0){ //定制
            Custom custom = customService.getById(createPhotoDto.getItemId());
            if(null==custom){
                picVo.setMsg("规格不存在");
                return picVo;
            }
            if(!custom.getUserId().equals(createPhotoDto.getUserId())){
                picVo.setMsg("非法请求");
                return picVo;
            }
            createPhotoDto.setHeight(custom.getHeightPx());
            createPhotoDto.setWidth(custom.getWidthPx());
            createPhotoDto.setDpi(custom.getDpi());
        }else { //列表
             item = itemService.getById(createPhotoDto.getItemId());
            if(null==item){
                picVo.setMsg("规格信息不存在");
                return picVo;
            }
            createPhotoDto.setHeight(item.getHeightPx());
            createPhotoDto.setWidth(item.getWidthPx());
            createPhotoDto.setDpi(item.getDpi());
        }



        try {
            RestTemplate restTemplate = new RestTemplate();

            // 构建 multipart 数据
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("input_image_base64", createPhotoDto.getImage());
            body.add("height",createPhotoDto.getHeight());
            body.add("width", createPhotoDto.getWidth());
            body.add("dpi",createPhotoDto.getDpi());
            body.add("human_matting_model",humanMattingModel);
            body.add("face_detect_model",faceDetectModel);
            body.add("hd",false);  //减少时间，初始化时不生成高清
            body.add("face_alignment",true);  //人脸对齐

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    zjzDomain+"/idphoto",
                    HttpMethod.POST,
                    requestEntity,
                    String.class);

            HivisionDto hivisionDto = JSON.parseObject(response.getBody(), HivisionDto.class);
            if(!hivisionDto.isStatus()){
                picVo.setMsg("未检测到人脸或多人脸");
                return picVo;
            }


            //保存生成记录
            Photo photo = new Photo();
            photo.setUserId(createPhotoDto.getUserId());
            if(null==item){
                photo.setName("用户自定义尺寸");
            }else {
                photo.setName(item.getName());
            }
            photo.setSize(createPhotoDto.getWidth()+"x"+createPhotoDto.getHeight());
            photo.setCreateTime(new Date());
            photoService.save(photo);

            //保存用户行为记录
            PhotoRecord record = new PhotoRecord();
            record.setName("生成证件照");
            record.setUserId(createPhotoDto.getUserId());
            record.setCreateTime(new Date());
            photoRecordService.save(record);

            //封装前端参数
            picVo.setId2(photo.getId());
            picVo.setOimg(createPhotoDto.getImage());
            picVo.setKimg(hivisionDto.getImageBase64Standard());
            picVo.setDpi(createPhotoDto.getDpi());
            return picVo;
        } catch (Exception e) {
            e.printStackTrace();
            picVo.setMsg("系统繁忙，请稍后再试");
            return picVo;
        }


    }

    @Override
    public PicVo createIdHdPhoto(CreatePhotoDto createPhotoDto) {
        PicVo picVo = new PicVo();
        Item item = null;
        if(createPhotoDto.getType()==0){ //定制
            Custom custom = customService.getById(createPhotoDto.getItemId());
            if(null==custom){
                picVo.setMsg("规格不存在");
                return picVo;
            }
            if(!custom.getUserId().equals(createPhotoDto.getUserId())){
                picVo.setMsg("非法请求");
                return picVo;
            }
            createPhotoDto.setHeight(custom.getHeightPx());
            createPhotoDto.setWidth(custom.getWidthPx());
            createPhotoDto.setDpi(custom.getDpi());
        }else { //列表
            item = itemService.getById(createPhotoDto.getItemId());
            if(null==item){
                picVo.setMsg("规格信息不存在");
                return picVo;
            }
            createPhotoDto.setHeight(item.getHeightPx());
            createPhotoDto.setWidth(item.getWidthPx());
            createPhotoDto.setDpi(item.getDpi());
        }



        try {
            RestTemplate restTemplate = new RestTemplate();

            // 构建 multipart 数据
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("input_image_base64", createPhotoDto.getImage());
            body.add("height",createPhotoDto.getHeight());
            body.add("width", createPhotoDto.getWidth());
            body.add("human_matting_model",humanMattingModel);
            body.add("face_detect_model",faceDetectModel);
            body.add("hd",true);
            body.add("face_alignment",true);  //人脸对齐

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    zjzDomain+"/idphoto",
                    HttpMethod.POST,
                    requestEntity,
                    String.class);

            HivisionDto hivisionDto = JSON.parseObject(response.getBody(), HivisionDto.class);
            if(!hivisionDto.isStatus()){
                picVo.setMsg("未检测到人脸或多人脸");
                return picVo;
            }


            //保存用户行为记录
            PhotoRecord record = new PhotoRecord();
            record.setName("生成高清证件照");
            record.setUserId(createPhotoDto.getUserId());
            record.setCreateTime(new Date());
            photoRecordService.save(record);

            //封装前端参数
            picVo.setKimg(hivisionDto.getImageBase64Hd());
            return picVo;
        } catch (Exception e) {
            e.printStackTrace();
            picVo.setMsg("系统繁忙，请稍后再试");
            return picVo;
        }

    }

    @Override
    public PicVo updateIdPhoto(CreatePhotoDto createPhotoDto) {
        PicVo picVo = new PicVo();
        try {
            RestTemplate restTemplate = new RestTemplate();

            // 构建 multipart 数据
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("input_image_base64", createPhotoDto.getImage());
            body.add("render",createPhotoDto.getRender());
            body.add("color",createPhotoDto.getColors());
            //非高清下载时传输dpi
            if(createPhotoDto.getDpi()>0){
                body.add("dpi",createPhotoDto.getDpi());
            }
            //如果用户设置了kb
            if(createPhotoDto.getKb()>0){
                body.add("kb",createPhotoDto.getKb());
            }



            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    zjzDomain+"/add_background",
                    HttpMethod.POST,
                    requestEntity,
                    String.class);


            HivisionDto hivisionDto = JSON.parseObject(response.getBody(), HivisionDto.class);
            if(!hivisionDto.isStatus()){
                picVo.setMsg("未检测到人脸或多人脸");
                return picVo;
            }

            //保存用户行为记录
            PhotoRecord record = new PhotoRecord();
            record.setName("换背景");
            record.setUserId(createPhotoDto.getUserId());
            record.setCreateTime(new Date());
            photoRecordService.save(record);

            picVo.setCimg(hivisionDto.getImageBase64());
            return picVo;
        } catch (Exception e) {
            e.printStackTrace();
            picVo.setMsg("系统繁忙，请稍后再试");
            return picVo;
        }


    }





    @Override
    public PicVo updateUserPhonto(Integer userid,String img,Integer photoId) {
        PicVo picVo = new PicVo();
        //防止被当图床
        Photo photo = photoService.getById(photoId);
        if(null==photo){
            picVo.setMsg("非法请求");
            return picVo;
        }
        if(!photo.getUserId().equals(userid)){
            picVo.setMsg("非法请求");
            return picVo;
        }



        //因为图片没刚开始存库，是为了防止性能浪费，所有由前端传入
        //将图片转成MultipartFile，再次检查，防止数据伪造，如：被劫持数据包上传黄色，木马什么的
        MultipartFile file = PicUtil.base64ToMultipartFile(img);


        // 检查文件类型
        String originalFilename = file.getOriginalFilename();
        if (!StringUtils.hasText(originalFilename) || (!originalFilename.toLowerCase().endsWith(".png")
                && !originalFilename.toLowerCase().endsWith(".jpg")) && !originalFilename.toLowerCase().endsWith(".jpeg")) {
            picVo.setMsg("图片类型不合法，仅支持jpg/png/jpeg的图片");
            return picVo;
        }

        //不检查图片大小，因为生成的证件照可能很大
//        if (file.getSize() > 15 * 1024 * 1024) {
//            picVo.setMsg("图片大小不能超过20M");
//            return picVo;
//        }

        //不开启鉴黄，因为测试生成后的图片存在误判
//        WebSet webSet = webSetService.getById(1);
//        //如果开启鉴黄
//        if(webSet.getSafetyApi()==2){
//            String s = uploadService.checkNsfw(file);
//            if(s!=null){
//                picVo.setMsg(s);
//                return picVo;
//            }
//        }


       //检查通过，上传服务器，数据库保存url
       //之前试过保存base64，发现数据库加载很慢，性能很低
        try {
        //按照日期保存文件
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String folderName = dateFormat.format(new Date());
        String filename = PicUtil.filesCopy(folderName, directory, originalFilename, file);



        String imagePath = picDomain + folderName + "/" + filename;
        photo.setId(photoId);
        photo.setNImg(imagePath);
        photoService.updateById(photo);


        //保存用户行为记录
        PhotoRecord record = new PhotoRecord();
        record.setName("下载证件照");
        record.setUserId(userid);
        record.setCreateTime(new Date());
        photoRecordService.save(record);
        picVo.setPicUrl(imagePath);
        return picVo;

        } catch (Exception e) {
            e.printStackTrace();
            picVo.setMsg("图片存入失败错误，请重试");
            return picVo;
        }

    }




}
