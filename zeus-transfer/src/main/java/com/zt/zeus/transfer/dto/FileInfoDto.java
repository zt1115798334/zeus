package com.zt.zeus.transfer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class FileInfoDto {
    private final String filename;

    private final String content;
}
