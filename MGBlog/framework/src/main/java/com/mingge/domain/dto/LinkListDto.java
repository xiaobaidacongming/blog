package com.mingge.domain.dto;

import lombok.Data;

@Data
public class LinkListDto {
    private String name;
    private String status;
    private Integer pageNum;
    private Integer pageSize;
}
