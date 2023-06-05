package com.mingge.service.impl;

import com.aliyun.oss.*;
import com.aliyun.oss.common.comm.ResponseMessage;
import com.aliyun.oss.model.*;
import com.mingge.config.OSSConfiguration;
import com.mingge.domain.ResponseResult;
import com.mingge.enums.AppHttpCodeEnum;
import com.mingge.exception.SystemException;
import com.mingge.utils.PathUtils;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class OSSService {
    public static Logger log = LoggerFactory.getLogger(OSSService.class);

    @Autowired
    private OSSConfiguration ossConfiguration;
    @Autowired
    private OSS ossClient;

    /**
     * 上传文件到阿里云 OSS 服务器
     * 下面有优化后的代码uploadFile1方法
     */
    public ResponseResult uploadFile(MultipartFile file) {
        String originalFilename =file.getOriginalFilename();
        if (!isImageFile(Objects.requireNonNull(originalFilename))){//该方法不抛nullPoint异常
            throw new SystemException(AppHttpCodeEnum.FILE_TYPE_ERROR);
        }
        // 获取文件全路径名
        String fileName = PathUtils.generateFilePath(originalFilename);
        try {
            // 上传文件的输入流
            InputStream inputStream = file.getInputStream();
            // 设置对象
            ObjectMetadata objectMetadata = new ObjectMetadata();
            // 设置数据流里有多少个字节可以读取
            objectMetadata.setContentLength(inputStream.available());
            objectMetadata.setCacheControl("no-cache");
            objectMetadata.setHeader("Pragma", "no-cache");
            objectMetadata.setContentType(file.getContentType());
            objectMetadata.setContentDisposition("inline;filename=" + fileName);
            // 上传文件
            ossClient.putObject(ossConfiguration.getBucketName(), fileName, inputStream, objectMetadata);
        } catch (IOException e) {
            log.error("发生异常: {}", e.getMessage(), e);
        }
        String downLoadPath = ossConfiguration.getDownloadPath()+fileName;
        log.info("文件下载路径:{}",downLoadPath);
        return ResponseResult.okResult(downLoadPath); // path+fileName
    }

    /**
     * 下载文件
     * 详细请参看“SDK手册 > Java-SDK > 上传文件”
     * 链接：https://help.aliyun.com/document_detail/oss/sdk/java-sdk/download_object.html?spm=5176.docoss/sdk/java-sdk/manage_object
     *
     * @param os
     * @param objectName
     */
    public void exportFile(OutputStream os, String objectName) {
        // ossObject包含文件所在的存储空间名称、文件名称、文件元信息以及一个输入流
        OSSObject ossObject = ossClient.getObject(ossConfiguration.getBucketName(), objectName);
        // 读取文件内容
        BufferedInputStream in = new BufferedInputStream(ossObject.getObjectContent());
        BufferedOutputStream out = new BufferedOutputStream(os);
        byte[] buffer = new byte[1024];
        int lenght;
        try {
            while ((lenght = in.read(buffer)) != -1) {
                out.write(buffer, 0, lenght);
            }
            out.flush();
        } catch (IOException e) {
            log.error("Error occurred: {}", e.getMessage(), e);
        }
    }

    /**
     * 获取 url
     *
     * @param filename
     * @param expSeconds
     * @return
     */
    public String getSingeNatureUrl(String filename, int expSeconds) {
        Date expiration = new Date(System.currentTimeMillis() + expSeconds * 1000);
        URL url = ossClient.generatePresignedUrl(ossConfiguration.getBucketName(), filename, expiration);
        if (url != null) {
            return url.toString();
        }
        return null;
    }

    /**
     * 删除文件
     * 详细请参看“SDK手册 > Java-SDK > 管理文件”
     * 链接：https://help.aliyun.com/document_detail/oss/sdk/java-sdk/manage_object.html?spm=5176.docoss/sdk/java-sdk/manage_bucket
     *
     * @param fileName
     */
    public void deleteFile(String fileName) {
        try {
            ossClient.deleteObject(ossConfiguration.getBucketName(), fileName);
        } catch (Exception e) {
            log.error("Error occurred: {}", e.getMessage(), e);
        }
    }

    /**
     * 判断文件是否存在
     *
     * @param fileName
     * @return
     */
    public boolean doesObjectExist(String fileName) {
        try {
            if (Strings.isEmpty(fileName)) {
                log.error("文件名不能为空");
                return false;
            } else {
                return ossClient.doesObjectExist(ossConfiguration.getBucketName(), fileName);
            }
        } catch (OSSException | ClientException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 查看 Bucket 中的 Object 列表
     * 详细请参看“SDK手册 > Java-SDK > 管理文件”
     * 链接：https://help.aliyun.com/document_detail/oss/sdk/java-sdk/manage_object.html?spm=5176.docoss/sdk/java-sdk/manage_bucket
     *
     * @return
     */
    public List<String> listObjects() {
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest(ossConfiguration.getBucketName()).withMaxKeys(200);
        ObjectListing objectListing = ossClient.listObjects(listObjectsRequest);
        List<OSSObjectSummary> objectSummaries = objectListing.getObjectSummaries();
        return objectSummaries.stream().map(OSSObjectSummary::getKey).collect(Collectors.toList());
    }

    /**
     * 设置文件访问权限
     * 详细请参看“SDK手册 > Java-SDK > 管理文件”
     * 链接：https://help.aliyun.com/document_detail/84838.html
     *
     * @param fileName
     */
    public void setObjectAcl(String fileName) {
        ossClient.setObjectAcl(ossConfiguration.getBucketName(), fileName, CannedAccessControlList.PublicRead);
    }

    public static boolean isImageFile(String fileName) {
        String[] imageExtensions = {"jpg", "jpeg", "png", "gif", "bmp"};
        String extension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
        for (String ext : imageExtensions) {
            if (extension.equals(ext)) {
                return true;
            }
        }
        return false;
    }

    public ResponseResult uploadFile1(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (!isImageFile(Objects.requireNonNull(originalFilename))) {
            throw new SystemException(AppHttpCodeEnum.FILE_TYPE_ERROR);
        }
        String fileName = PathUtils.generateFilePath(originalFilename);
        // 资源管理：使用try-with-resources语句可以更好地管理文件流资源，确保及时关闭文件流，避免资源泄漏。
        try (InputStream inputStream = file.getInputStream()) {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            //避免使用inputStream.available()：inputStream.available()方法返回的是剩余可读取的字节数，并不一定是整个文件的大小，
            // 因此在设置Content-Length时可能会得到不准确的值。建议使用其他方法来获取文件的大小，例如使用file.getSize()方法。
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setCacheControl("no-cache");
            objectMetadata.setHeader("Pragma", "no-cache");
            objectMetadata.setContentType(file.getContentType());
            objectMetadata.setContentDisposition("inline;filename=" + fileName);
            ossClient.putObject(ossConfiguration.getBucketName(), fileName, inputStream, objectMetadata);
        } catch (IOException e) {
            log.error("发生异常: {}", e.getMessage(), e);
            // 在出现异常时，抛出自定义的异常或返回适当的错误响应，以便客户端能够正确处理错误情况。
            throw new SystemException(AppHttpCodeEnum.UPLOAD_IMG_ERROR);
        }

        String downLoadPath = ossConfiguration.getDownloadPath() + fileName;
        log.info("文件访问链接: {}", downLoadPath);
        return ResponseResult.okResult(downLoadPath);
    }
}