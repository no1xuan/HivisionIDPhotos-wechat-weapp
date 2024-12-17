package org.zjzWx.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.zjzWx.entity.Photo;
import org.zjzWx.service.PhotoService;

@Component
public class PhotoTask {
    @Autowired
    private PhotoService photoService;

    //每天00:00删除仅生成的垃圾数据    垃圾数据指：用户仅生成但是没点击下载按钮的数据
    @Scheduled(cron = "0 0 0 * * ?")
    public void executeTask() {
        QueryWrapper<Photo> qw = new QueryWrapper<>();
        qw.isNull("n_img");
        photoService.remove(qw);
    }


}
