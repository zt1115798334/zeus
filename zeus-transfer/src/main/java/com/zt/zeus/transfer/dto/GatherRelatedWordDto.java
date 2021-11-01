package com.zt.zeus.transfer.dto;

import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GatherRelatedWordDto {
    private Long id;

    /**
     * 采集词
     */
    private String name;

    public GatherRelatedWordDto(String name) {
        this.name = name;
    }
}
