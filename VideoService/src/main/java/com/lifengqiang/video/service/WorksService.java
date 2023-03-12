package com.lifengqiang.video.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lifengqiang.video.model.entity.Works;
import com.lifengqiang.video.model.result.WorksResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface WorksService extends IService<Works> {
    WorksResult getResult(Works works);

    void addImagesWorks(int userId, String content, MultipartFile[] files)throws IOException;

    void addVideoWorks(int userId, String content, MultipartFile file) throws IOException;
}
