package com.mingge.domain.vo;

import lombok.Data;

@Data
public class MenuById {
    private Long id;
    //菜单图标
    private String icon;
    //菜单名称
    private String menuName;
    //菜单类型（M目录 C菜单 F按钮）
    private String menuType;
    //显示顺序
    private Integer orderNum;
    //父菜单ID
    private Long parentId;
    //路由地址
    private String path;
    //菜单状态（0正常 1停用）
    private String status;
    //备注
    private String remark;
    //菜单状态（0显示 1隐藏）
    private String visible;

    //组件路径
    private String component;
    //权限标识
    private String perms;
}
