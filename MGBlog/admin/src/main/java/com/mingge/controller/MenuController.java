package com.mingge.controller;

import com.mingge.domain.ResponseResult;
import com.mingge.domain.entity.Menu;
import com.mingge.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/menu")
public class MenuController {
    @Autowired
    private MenuService menuService;

    /**
     * 获取菜单树
     */
    @GetMapping("/treeselect")
    public ResponseResult treeselect(){
        return menuService.treeselect();
    }
    @GetMapping("/roleMenuTreeselect/{id}")
    public ResponseResult roleMenuTreeselect(@PathVariable("id")Long id){
        return menuService.roleMenuTreeselect(id);
    }
    @GetMapping("/list")
    public ResponseResult listMenus(@RequestParam(name = "status",defaultValue = "")String status,
                                    @RequestParam(name="menuName",defaultValue = "")String menuName){
        return menuService.getMenus(status,menuName);
    }
    @GetMapping("/{id}")
    public ResponseResult getMenu(@PathVariable("id")Long id){
        return menuService.getMenu(id);
    }
    @PostMapping
    public ResponseResult addMenu(@RequestBody Menu menu){
        menuService.save(menu);
        return ResponseResult.okResult();
    }
    @PutMapping
    public ResponseResult putMenu(@RequestBody Menu menu){
        return menuService.putMuen(menu);
    }
    @DeleteMapping("/{menuId}")
    public ResponseResult deleteMuen(@PathVariable("menuId")Long id){
        return menuService.deleteMenu(id);
    }
}
