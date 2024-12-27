package org.zjzWx.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.zjzWx.dao.PhotoDao;
import org.zjzWx.entity.Photo;
import org.zjzWx.service.PhotoService;
import org.springframework.stereotype.Service;
import org.zjzWx.util.PicUtil;

import java.util.List;


@Service
public class PhotoServiceImpl extends ServiceImpl<PhotoDao,Photo> implements PhotoService {

    @Value("${webset.directory}")
    private String directory;

    @Override
    public Page<Photo> photoList(int pageNum, int pageSize, String userId) {
        Page<Photo> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Photo> qw = new QueryWrapper<>();
        qw.select("id","name","n_img","size","create_time");
        qw.eq("user_id",userId);
        qw.isNotNull("n_img");
        qw.orderByDesc("create_time");
        return baseMapper.selectPage(page, qw);
    }

    @Override
    public void deletePhotoId(Integer id, String userId) {
        QueryWrapper<Photo> qw = new QueryWrapper<>();
        qw.eq("id",id);
        qw.eq("user_id",userId);
        Photo photo = baseMapper.selectOne(qw);
        if(null!=photo){
            PicUtil.deleteImage(photo.getNImg(),directory);
            baseMapper.deleteById(photo);
        }
    }


}
