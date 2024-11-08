package org.zjzWx.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminIndexVo {
    //当天操作
    private long makeNum;
    //总操作
    private long makeTotal;
    //当天新增用户
    private long userNum;
    //总用户
    private long userTotal;
    //支持的规格（包含自定义）
    private long itemTotal;

    //图表
    private ChartDataVo chartDataVo;
}
