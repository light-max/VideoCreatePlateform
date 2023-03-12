package com.lifengqiang.video.controller.api;

import com.lifengqiang.video.model.entity.User;
import com.lifengqiang.video.service.WorksService;
import com.lifengqiang.video.util.UseDefaultSuccessResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

@Controller
public class WorksController {
    @Resource
    WorksService service;

    @PostMapping("/user/works/images")
    @ResponseBody
    @UseDefaultSuccessResponse
    public void submitImagesWorks(
            @SessionAttribute("user") User user,
            String content,
            MultipartFile[] files
    ) throws IOException {
        service.addImagesWorks(1, content, files);
    }

    @PostMapping("/user/works/video")
    @ResponseBody
    @UseDefaultSuccessResponse
    public void submitVideoWorks(
            @SessionAttribute("user") User user,
            String content,
            MultipartFile file
    ) throws IOException {
        service.addVideoWorks(user.getId(), content, file);
    }
}
