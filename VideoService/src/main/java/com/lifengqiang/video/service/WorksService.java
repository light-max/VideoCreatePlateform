package com.lifengqiang.video.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lifengqiang.video.model.entity.Works;
import com.lifengqiang.video.model.result.WorksResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface WorksService extends IService<Works> {
    /**
     * @param currentUserId 没有就传-1
     */
    WorksResult getResult(int currentUserId, Works works);

    void addImagesWorks(int userId, String content, MultipartFile[] files) throws IOException;

    void addVideoWorks(int userId, String content, MultipartFile file, MultipartFile cover) throws IOException;

    /**
     * @param currentUserId 没有就传-1
     */
    List<Integer> getRandomWorks(int currentUserId, int size);
}
