package org.zjzWx.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.zjzWx.entity.Photo;
import org.zjzWx.service.PhotoService;
import org.zjzWx.util.PicUtil;

import java.util.List;

@Component
public class PhotoTask {


    @Value("${webset.directory}")
    private String directory;
    @Autowired
    private PhotoService photoService;

    //每天00:00删除仅生成的垃圾数据    垃圾数据指：用户仅生成但是没点击下载按钮的数据
    @Scheduled(cron = "0 0 0 * * ?")
    public void executeTask() {
        QueryWrapper<Photo> qw = new QueryWrapper<>();
        qw.isNull("n_img");
        List<Photo> list = photoService.list(qw);
        for (Photo photo : list) {
            PicUtil.deleteImage(photo.getNImg(),directory);
            photoService.removeById(photo);
        }

    }


}
