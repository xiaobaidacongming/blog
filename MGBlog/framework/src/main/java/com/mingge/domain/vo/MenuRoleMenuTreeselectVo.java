package com.mingge.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuRoleMenuTreeselectVo {
    private List<MenuTreeselectVo> menus;
}
