package com.lifengqiang.video.controller.api;

import com.lifengqiang.video.constant.GlobalConstant;
import com.lifengqiang.video.model.entity.User;
import com.lifengqiang.video.util.FileTools;
import com.lifengqiang.video.util.UseDefaultSuccessResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Controller
public class HeadController {
    @Value("${file.images.head}")
    private String headImagesPath;

    @GetMapping("/head/raw/{id}")
    @ResponseBody
    public ResponseEntity<FileSystemResource> getHeadImage(@PathVariable Integer id) {
        File file = FileTools.getHeadImagePath(headImagesPath, id);
        return ResponseEntity.ok(new FileSystemResource(file));
    }

    @GetMapping("/head/{id}")
    public String getHead(@PathVariable Integer id) {
        File file = FileTools.getHeadImagePath(headImagesPath, id);
        if (file.exists()) {
            return "redirect:/head/raw/" + id;
        } else {
            return "redirect:/images/default-head.jpg";
        }
    }

    @PostMapping("/user/head")
    @ResponseBody
    @UseDefaultSuccessResponse
    public void setHead(@SessionAttribute("user") User user, MultipartFile file) {
        try {
            File filePath = FileTools.getHeadImagePath(headImagesPath, user.getId());
            FileTools.write(file.getInputStream(), filePath);
        } catch (IOException e) {
            GlobalConstant.error.newException();
        }
    }
}
