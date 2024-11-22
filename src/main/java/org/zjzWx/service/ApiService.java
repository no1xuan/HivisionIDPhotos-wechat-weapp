package org.zjzWx.service;

import org.zjzWx.model.dto.CreatePhotoDto;
import org.zjzWx.model.vo.PicVo;

//证件照相关API
public interface ApiService {


 //生成证件照，初始化，返回原图（用于下载高清），透明图（用于切换颜色）
 PicVo createIdPhoto(CreatePhotoDto createPhotoDto);

 //生成高清证件照
 PicVo createIdHdPhoto(CreatePhotoDto createPhotoDto);

 //换背景色
 PicVo updateIdPhoto(CreatePhotoDto createPhotoDto);


 //更新用户保存记录
 PicVo updateUserPhonto(Integer userid,String img,Integer photoId);



}
