package com.jiahe.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/common")
/**
 * 文件的上传和下载
 */
public class CommonController {

    @Value("${jiahe.path}")
    private String basePath;

    @PostMapping("/upload")
    public String upload(MultipartFile file) {
        //file是一个临时文件，需要转存到指定位置，否则本次请求完成后临时文件会删除
        //原始文件名
        String originalFilename = file.getOriginalFilename(); //abc.jpg
        //substring (int begin) 截取下标begin - end的字符串
        //lastIndexOf(".") 获取.最后一次出现出现的下标
        String suffix = originalFilename.substring(originalFilename.lastIndexOf(".")); //截取到后缀名
        //使用UUID重新生成文件名，防止文件名称重复造成文件覆盖
        String fileName = UUID.randomUUID().toString() + suffix;
        //创建一个目录对象
        File dir = new File(basePath);
        //判断目录是否存在
        if (!dir.exists()) {
            //目录不存在，需要创建
            dir.mkdir();
        }
        try {
            //将临时文件转存到指定位置
            file.transferTo(new File(basePath + fileName));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return fileName;
    }

    /**
     * 文件下载
     * @param name
     * @param response
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) {
        //输入流，通过输入流读取文件内容
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));
            //输出流，通过输出流将文件写回浏览器，在浏览器显示图片
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("image/jpeg");
            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes)) != -1) {
                outputStream.write(bytes,0, len);
                outputStream.flush();
            }
            //关闭资源
            outputStream.close();
            fileInputStream.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
