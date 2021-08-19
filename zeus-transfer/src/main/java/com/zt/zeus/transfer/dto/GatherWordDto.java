package com.zt.zeus.transfer.dto;

import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GatherWordDto {
    private Long id;

    /**
     * 采集词
     */
    private String name;

    public GatherWordDto(String name) {
        this.name = name;
    }
}
