package org.zjzWx.model.vo;

import java.util.List;

public class ChartDataVo {
    private List<String> time;
    private List<Integer> data;

    public List<String> getTime() {
        return time;
    }

    public void setTime(List<String> time) {
        this.time = time;
    }

    public List<Integer> getData() {
        return data;
    }

    public void setData(List<Integer> data) {
        this.data = data;
    }
}
