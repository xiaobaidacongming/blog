package com.mingge.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class MenuTreeselectVo {
    private Long id;
    // 标签名->menu_name
    private String label;
    //父菜单ID
    private Long parentId;
    private List<MenuTreeselectVo> children;
}
