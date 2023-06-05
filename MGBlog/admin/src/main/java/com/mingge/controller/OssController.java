package com.mingge.controller;

import com.mingge.domain.ResponseResult;
import com.mingge.service.impl.OSSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping
public class OssController {
    @Autowired
    private OSSService ossService;
    @PostMapping("/upload")
    public ResponseResult uploadImg(@RequestParam("img")MultipartFile file){
        return ossService.uploadFile1(file);
    }
}
