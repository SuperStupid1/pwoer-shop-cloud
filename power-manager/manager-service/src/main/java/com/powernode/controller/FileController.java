package com.powernode.controller;

import cn.hutool.core.io.FileUtil;
import com.alibaba.spring.util.FieldUtils;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;

/**
 * 信息描述
 *
 * @author DuBo
 * @createDate 2022/7/29 15:06
 */
@RestController
@RequestMapping("admin/file/upload")
@Api(tags = "图片上传")
public class FileController {

    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    @Value("${resources.url}")
    private String hostAdr;

    @PostMapping("element")
    @ApiOperation("文件图片上传")
    public ResponseEntity<String> uploadImages(@RequestParam("file") MultipartFile file){
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String originalFilename = file.getOriginalFilename();
        String extName = FileUtil.extName(originalFilename);
        // 这里获取到的路径还没有加上服务器的ip
        StorePath storePath = fastFileStorageClient.uploadFile(inputStream, file.getSize(), extName, new HashSet<>());
        return ResponseEntity.ok(hostAdr + storePath.getFullPath());
    }
}
