package org.zjzWx.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.zjzWx.entity.Photo;
import org.zjzWx.entity.PhotoRecord;
import org.zjzWx.model.dto.ColourizeDto;
import org.zjzWx.model.dto.ExploreDto;
import org.zjzWx.model.dto.ExploreIndexDto;
import org.zjzWx.model.dto.HivisionDto;
import org.zjzWx.service.OtherApiService;
import org.zjzWx.service.PhotoRecordService;
import org.zjzWx.service.PhotoService;
import org.zjzWx.util.PicUtil;

import java.util.Date;


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

    @Autowired
    private PhotoService photoService;
    @Autowired
    private PhotoRecordService photoRecordService;



    @Override
    public ExploreIndexDto exploreDtoCount() {
        ExploreIndexDto exploreIndexDto = new ExploreIndexDto();

        QueryWrapper<PhotoRecord> qw1  = new QueryWrapper<>();
        qw1.ne("name", "生成黑白图片上色")
                .ne("name", "生成高清证件照");
        exploreIndexDto.setZjzCount(photoRecordService.count(qw1));

        QueryWrapper<PhotoRecord> qw2  = new QueryWrapper<>();
        qw2.eq("name", "生成六寸排版照");
        exploreIndexDto.setGenerateLayoutCount(photoRecordService.count(qw2));

        QueryWrapper<PhotoRecord> qw3  = new QueryWrapper<>();
        qw3.eq("name", "生成黑白图片上色");
        exploreIndexDto.setColourizeCount(photoRecordService.count(qw3));

        QueryWrapper<PhotoRecord> qw4  = new QueryWrapper<>();
        qw4.eq("name", "生成图片抠图");
        exploreIndexDto.setMattingCount(photoRecordService.count(qw4));


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
            if(null!=colourizeDto && 2==colourizeDto.getStatus()){
                //base64转MultipartFile，进行保存文件并返回url
                MultipartFile file = PicUtil.base64ToMultipartFile(colourizeDto.getProcessedImage());
                String originalFilename = file.getOriginalFilename();
                String filename = PicUtil.filesCopy("colourize", directory, originalFilename, file);



                String imagePath = picDomain + "colourize" + "/" + filename;
                Photo photo = new Photo();
                photo.setUserId(exploreDto.getUserId());
                photo.setName("黑白图片上色");
                photo.setNImg(imagePath);
                photo.setSize("无规格");
                photo.setCreateTime(new Date());
                photoService.save(photo);


                //保存用户行为记录
                PhotoRecord record = new PhotoRecord();
                record.setName("生成黑白图片上色");
                record.setUserId(exploreDto.getUserId());
                record.setCreateTime(new Date());
                photoRecordService.save(record);

                return imagePath;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }

    @Override
    public String matting(ExploreDto exploreDto) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            // 构建 multipart 数据
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("input_image",new PicUtil.MultipartInputStreamFileResource(PicUtil.base64ToMultipartFile(exploreDto.getProcessedImage())));
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
            body.add("input_image_base64",exploreDto.getProcessedImage());
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
            String filename = PicUtil.filesCopy("matting", directory, originalFilename, file);



            String imagePath = picDomain + "matting" + "/" + filename;
            Photo photo = new Photo();
            photo.setUserId(exploreDto.getUserId());
            photo.setName("六寸排版照");
            photo.setNImg(imagePath);
            photo.setSize("无规格");
            photo.setCreateTime(new Date());
            photoService.save(photo);


            //保存用户行为记录
            PhotoRecord record = new PhotoRecord();
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


}
