package org.zjzWx.util;

import lombok.Data;

import java.io.Serializable;

@Data
public class R<T> implements Serializable {

    private static final long serialVersionUID = -6630747483482976634L;

    private Integer code;
    private String msg;
    private T data;


    public R(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }



    public static <T>R<T> ok(T data) {
        return new R<>(200,"请求成功",data);
    }
    public static <T>R<T> no() {
        return new R<>(404,"暂无数据",null);
    }
    public static <T>R<T> no(T data) {
        return new R<>(404,"操作失败",data);
    }


}
