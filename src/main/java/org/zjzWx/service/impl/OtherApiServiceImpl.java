package org.zjzWx.service.impl;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.zjzWx.entity.Photo;
import org.zjzWx.entity.PhotoRecord;
import org.zjzWx.model.dto.ColourizeDto;
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

    @Value("${webset.colourizeDomain}")
    private String colourizeDomain;
    ;
    @Autowired
    private PhotoService photoService;
    @Autowired
    private PhotoRecordService photoRecordService;


    @Override
    public String colourize(Integer userId, String img) {

        try {

            //构建发起请求
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
            requestBody.add("base64_image", img);

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
                photo.setUserId(userId);
                photo.setName("黑白图片上色");
                photo.setNImg(imagePath);
                photo.setSize("无规格");
                photo.setCreateTime(new Date());
                photoService.save(photo);


                //保存用户行为记录
                PhotoRecord record = new PhotoRecord();
                record.setName("生成黑白图片上色");
                record.setUserId(userId);
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



}
