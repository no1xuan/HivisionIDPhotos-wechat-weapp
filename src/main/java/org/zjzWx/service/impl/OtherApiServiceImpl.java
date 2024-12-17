package org.zjzWx.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.zjzWx.entity.AppSet;
import org.zjzWx.entity.Photo;
import org.zjzWx.entity.PhotoRecord;
import org.zjzWx.model.dto.*;
import org.zjzWx.service.*;
import org.zjzWx.util.PicUtil;

import java.time.*;
import java.util.Date;
import java.util.List;


@Service
public class OtherApiServiceImpl implements OtherApiService {

    @Value("${webset.directory}")
    private String directory;

    @Value("${webset.picDomain}")
    private String picDomain;

    @Value("${webset.mattingDomain}")
    private String mattingDomain;

    @Value("${webset.colourizeDomain}")
    private String colourizeDomain;

    @Value("${modelset.mattingModel}")
    private String mattingModel;

    @Value("${webset.cartoonDomain}")
    private String cartoonDomain;

    @Autowired
    private PhotoService photoService;
    @Autowired
    private PhotoRecordService photoRecordService;
    @Autowired
    private AppSetService appSetService;



    @Override
    public ExploreIndexDto exploreDtoCount() {
        ExploreIndexDto exploreIndexDto = new ExploreIndexDto();
        List<AppSet> list = appSetService.list();
        for (AppSet appSet : list) {

            if(appSet.getType()==3){
                QueryWrapper<PhotoRecord> qw1  = new QueryWrapper<>();
                qw1.in("type",1,2,3,4,10);
                exploreIndexDto.setZjzCount(photoRecordService.count(qw1));
            }

            if(appSet.getType()==4){
                if(appSet.getStatus()==0){
                    exploreIndexDto.setGenerateLayoutCount(-1L);
                }else {
                    QueryWrapper<PhotoRecord> qw2  = new QueryWrapper<>();
                    qw2.eq("type",7);
                    exploreIndexDto.setGenerateLayoutCount(photoRecordService.count(qw2));
                }
            }

            if(appSet.getType()==5){
                if(appSet.getStatus()==0){
                    exploreIndexDto.setColourizeCount(-1L);
                }else {
                    QueryWrapper<PhotoRecord> qw3  = new QueryWrapper<>();
                    qw3.eq("type",5);
                    exploreIndexDto.setColourizeCount(photoRecordService.count(qw3));
                }
            }

            if(appSet.getType()==6){
                if(appSet.getStatus()==0){
                    exploreIndexDto.setMattingCount(-1L);;
                }else {
                    QueryWrapper<PhotoRecord> qw4  = new QueryWrapper<>();
                    qw4.eq("type",6);
                    exploreIndexDto.setMattingCount(photoRecordService.count(qw4));
                }
            }
            if(appSet.getType()==7){
                if(appSet.getStatus()==0){
                    exploreIndexDto.setImageDefinitionEnhanceCount(-1L);;
                }else {
                    QueryWrapper<PhotoRecord> qw5  = new QueryWrapper<>();
                    qw5.eq("type",9);
                    exploreIndexDto.setImageDefinitionEnhanceCount(photoRecordService.count(qw5));
                }
            }

            if(appSet.getType()==8){
                if(appSet.getStatus()==0){
                    exploreIndexDto.setCartoonCount(-1L);;
                }else {
                    QueryWrapper<PhotoRecord> qw6  = new QueryWrapper<>();
                    qw6.eq("type",8);
                    exploreIndexDto.setCartoonCount(photoRecordService.count(qw6));
                }
            }

        }


        return exploreIndexDto;
    }


    @Override
    public String colourize(ExploreDto exploreDto) {

        try {

            //构建发起请求
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
            requestBody.add("base64_image", exploreDto.getProcessedImage());

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(
                    colourizeDomain+"colourizeImg",
                    HttpMethod.POST,
                    requestEntity,
                    String.class);


            ColourizeDto colourizeDto = JSON.parseObject(response.getBody(), ColourizeDto.class);
            if(null==colourizeDto && 2!=colourizeDto.getStatus()) {
                return null;
            }

                //base64转MultipartFile，进行保存文件并返回url
                MultipartFile file = PicUtil.base64ToMultipartFile(colourizeDto.getProcessedImage());
                String originalFilename = file.getOriginalFilename();
                String filename = PicUtil.filesCopy("colourize", directory, originalFilename, file);



                String imagePath = picDomain + "colourize" + "/" + filename;
                Photo photo = new Photo();
                photo.setUserId(exploreDto.getUserId());
                photo.setName("老照片上色");
                photo.setNImg(imagePath);
                photo.setSize("无规格");
                photo.setCreateTime(new Date());
                photoService.save(photo);


                //保存用户行为记录
                PhotoRecord record = new PhotoRecord();
                record.setType(5);
                record.setName("生成老照片上色");
                record.setUserId(exploreDto.getUserId());
                record.setCreateTime(new Date());
                photoRecordService.save(record);

                return imagePath;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }

    @Override
    public String matting(ExploreDto exploreDto) {
        try {

            RestTemplate restTemplate = new RestTemplate();

            // 构建 multipart 数据
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            MultipartFile multipartFile = PicUtil.base64ToMultipartFile(exploreDto.getProcessedImage());
            body.add("input_image", new PicUtil.MultipartInputStreamFileResource(multipartFile));
            body.add("human_matting_model",mattingModel);
            if(null!=exploreDto.getDpi()){
                body.add("dpi",exploreDto.getDpi()); //代表用户输入了dpi
            }



            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    mattingDomain+"/human_matting",
                    HttpMethod.POST,
                    requestEntity,
                    String.class);


            HivisionDto hivisionDto = JSON.parseObject(response.getBody(), HivisionDto.class);
            if(!hivisionDto.isStatus()){
                return null;
            }

            //base64转MultipartFile，进行保存文件并返回url
            MultipartFile file = PicUtil.base64ToMultipartFile(hivisionDto.getImageBase64());
            String originalFilename = file.getOriginalFilename();
            String filename = PicUtil.filesCopy("matting", directory, originalFilename, file);



            String imagePath = picDomain + "matting" + "/" + filename;
            Photo photo = new Photo();
            photo.setUserId(exploreDto.getUserId());
            photo.setName("图片抠图");
            photo.setNImg(imagePath);
            photo.setSize("无规格");
            photo.setCreateTime(new Date());
            photoService.save(photo);


            //保存用户行为记录
            PhotoRecord record = new PhotoRecord();
            record.setType(6);
            record.setName("生成图片抠图");
            record.setUserId(exploreDto.getUserId());
            record.setCreateTime(new Date());
            photoRecordService.save(record);

            return imagePath;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public String generateLayoutPhotos(ExploreDto exploreDto) {
        try {

            RestTemplate restTemplate = new RestTemplate();

            // 构建 multipart 数据
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            MultipartFile multipartFile = PicUtil.base64ToMultipartFile(exploreDto.getProcessedImage());
            body.add("input_image", new PicUtil.MultipartInputStreamFileResource(multipartFile));
//            新版本HivisionIDPhotos的input_image_base64限制了1M，暂时停止使用base64传输
//            body.add("input_image_base64",exploreDto.getProcessedImage());
            if(null!=exploreDto.getHeight()){
                body.add("height",exploreDto.getHeight());  //代表用户输入了高度
            }
            if(null!=exploreDto.getWidth()){
                body.add("width",exploreDto.getWidth());  //代表用户输入了宽度
            }
            if(null!=exploreDto.getKb()){
                body.add("kb",exploreDto.getKb());  //代表用户输入了dpi
            }
            if(null!=exploreDto.getDpi()){
                body.add("dpi",exploreDto.getDpi());  //代表用户输入了kb
            }

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    mattingDomain+"/generate_layout_photos",
                    HttpMethod.POST,
                    requestEntity,
                    String.class);


            HivisionDto hivisionDto = JSON.parseObject(response.getBody(), HivisionDto.class);
            if(!hivisionDto.isStatus()){
                return null;
            }

            //base64转MultipartFile，进行保存文件并返回url
            MultipartFile file = PicUtil.base64ToMultipartFile(hivisionDto.getImageBase64());
            String originalFilename = file.getOriginalFilename();
            String filename = PicUtil.filesCopy("generateLayoutPhotos", directory, originalFilename, file);



            String imagePath = picDomain + "generateLayoutPhotos" + "/" + filename;
            Photo photo = new Photo();
            photo.setUserId(exploreDto.getUserId());
            photo.setName("六寸排版照");
            photo.setNImg(imagePath);
            photo.setSize("无规格");
            photo.setCreateTime(new Date());
            photoService.save(photo);


            //保存用户行为记录
            PhotoRecord record = new PhotoRecord();
            record.setType(7);
            record.setName("生成六寸排版照");
            record.setUserId(exploreDto.getUserId());
            record.setCreateTime(new Date());
            photoRecordService.save(record);

            return imagePath;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public String cartoon(ExploreDto exploreDto) {

        try {
            RestTemplate restTemplate = new RestTemplate();

            // 构建 multipart 数据
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> requestEntity = new HttpEntity<>("{\"image\": \"" + exploreDto.getProcessedImage() + "\"}", headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    cartoonDomain + "/cartoon",
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            CartoonDto cartoonDto = JSON.parseObject(response.getBody(), CartoonDto.class);
            if (cartoonDto == null || cartoonDto.getError() != null) {
                return null;
            }

            //base64转MultipartFile，进行保存文件并返回url
            MultipartFile file = PicUtil.base64ToMultipartFile(cartoonDto.getCartoonImage());
            String originalFilename = file.getOriginalFilename();
            String filename = PicUtil.filesCopy("cartoon", directory, originalFilename, file);

            String imagePath = picDomain + "cartoon" + "/" + filename;


            Photo photo = new Photo();
            photo.setUserId(exploreDto.getUserId());
            photo.setName("动漫风照");
            photo.setNImg(imagePath);
            photo.setSize("无规格");
            photo.setCreateTime(new Date());
            photoService.save(photo);


            //保存用户行为记录
            PhotoRecord record = new PhotoRecord();
            record.setType(8);
            record.setName("生成动漫风照");
            record.setUserId(exploreDto.getUserId());
            record.setCreateTime(new Date());
            photoRecordService.save(record);
            ;
            return imagePath;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public long checkTheFreeQuota(Integer type,Integer type2,Integer userId){
        QueryWrapper<AppSet> qw = new QueryWrapper<>();
        qw.eq("type",type);
        AppSet appSet = appSetService.getOne(qw);
        if(appSet.getStatus()==1){
            return -1L;
        }
        if(appSet.getStatus()==0){
            return 0L;
        }


        // 获取当前日期
        ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault());
        LocalDate today = now.toLocalDate();
        // 当天的开始时间
        LocalDateTime startOfDay = today.atStartOfDay(); // 默认时区
        // 当天的结束时间
        LocalDateTime endOfDay = LocalDateTime.of(today, LocalTime.MAX); // 使用当天的最后一刻


        // 统计当天的照片制作次数
        QueryWrapper<PhotoRecord> qw2 = new QueryWrapper<>();
        qw2.eq("type",type2);
        qw2.eq("user_id",userId);
        qw2.ge("create_time", startOfDay);
        qw2.le("create_time", endOfDay);
        long count = photoRecordService.count(qw2);
        if(count< appSet.getCounts()){
            return appSet.getCounts()-count;
        }

        return 0L;
    }






}
