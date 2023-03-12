package com.lifengqiang.video.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lifengqiang.video.mapper.WorksMapper;
import com.lifengqiang.video.model.entity.Works;
import com.lifengqiang.video.model.result.WorksResult;
import com.lifengqiang.video.service.WorksService;
import com.lifengqiang.video.util.FileTools;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Service
public class WorksServiceImpl extends ServiceImpl<WorksMapper, Works> implements WorksService {
    @Value("${file.works.images}")
    private String imagesPath;

    @Value("${file.works.videos}")
    private String videosPath;

    @Override
    public WorksResult getResult(Works works) {
        return null;
    }

    @Override
    public void addImagesWorks(int userId, String content, MultipartFile[] files) throws IOException {
        Works works = Works.builder()
                .userId(userId)
                .type(0)
                .content(content)
                .build()
                .check();
        File path = FileTools.getFileMakePath(imagesPath, Integer.toString(userId));
        if (!path.exists()) {
            path.mkdir();
        }
        FileTools.deleteChildFiles(path);
        for (int i = 0; i < files.length; i++) {
            File saveFile = new File(path, (i + 1) + ".jpg");
            InputStream in = files[i].getInputStream();
            FileTools.write(in, saveFile);
        }
        save(works);
    }

    @Override
    public void addVideoWorks(int userId, String content, MultipartFile file) throws IOException {
        Works works = Works.builder()
                .userId(userId)
                .type(0)
                .content(content)
                .build()
                .check();
        File path = FileTools.getFileMakePath(videosPath, Integer.toString(userId));
        if (!path.exists()) {
            path.mkdir();
        }
        File saveFile = new File(path, "video.mp4");
        FileTools.write(file.getInputStream(), saveFile);
        save(works);
    }
}
