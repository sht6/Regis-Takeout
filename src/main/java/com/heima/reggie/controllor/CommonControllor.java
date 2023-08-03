package com.heima.reggie.controllor;

import com.heima.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/common")
public class CommonControllor {

    @Value("${reggie.path}")
    private String basePath;

    /**
     * 文件上传
     * @param file
     * @return
     */
    @ResponseBody
    @PostMapping("/upload")
    public R<String> update(MultipartFile file){
        // file是一个临时文件，需要转存到指定位置，否则本次请求后临时文件会删除
        log.info("file:{}",file);

        // 原始文件名
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));


        //使用uuid随机生成
        String uuid = UUID.randomUUID().toString() + suffix;

        // 创建一个目录对象
        File file1 = new File(basePath);

        // 判断目录是否存在
        if(!file1.exists()){
//            不存在，创建一个
            file1.mkdirs();
        }


        // 文件进行一个转存到指定位置
        try {
            file.transferTo(new File(basePath+uuid));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return R.success(uuid);
    }

    /**
     * 文件下载
     * @param name
     * @param response
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
        log.info(name);
        try {
            //输入流，通过输入流读取文件内容
            FileInputStream fileInputStream = new FileInputStream(new File(basePath+name));
            // 输出流，通过输出流将文件写会浏览器，在浏览器展示图片
            ServletOutputStream outputStream = response.getOutputStream();

            response.setContentType("image/jpeg");

            int len = 0;
            byte[] bytes = new byte[1024];

            while ((len = fileInputStream.read(bytes)) != -1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }

            // 关闭资源
            outputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
