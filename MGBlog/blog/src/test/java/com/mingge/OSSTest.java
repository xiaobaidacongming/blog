package com.mingge;

import com.mingge.service.impl.OSSService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class OSSTest {
    @Autowired
    private OSSService ossService;
    @Test
    public void test(){
        System.out.println(1);
    }

    @Test
    public void test1(){
//        ossService.uploadFile();
    }
}
