package org.zjzWx.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExploreDto {
    //证件照数量
    private long zjzCount;
    //老照片数量
    private long colourizeCount;
    //抠图数量
    private long mattingCount;
}
