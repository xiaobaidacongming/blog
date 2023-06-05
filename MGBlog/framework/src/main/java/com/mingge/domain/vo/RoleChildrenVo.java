package com.mingge.domain.vo;

import lombok.Data;
import java.util.Date;
@Data
public class RoleChildrenVo extends RoleVo{
    private Long createBy;
    private Date createTime;
    private String delFlag;
    private String remark;
    private Long updateBy;
}
