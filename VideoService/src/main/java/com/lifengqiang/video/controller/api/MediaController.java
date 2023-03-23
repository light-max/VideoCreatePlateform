package com.lifengqiang.video.controller.api;

import com.lifengqiang.video.util.FileTools;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;

@Controller
public class MediaController {
    @Value("${file.works.images}")
    private String imagesPath;

    @Value("${file.works.videos}")
    private String videosPath;

    @GetMapping(value = "/media/image/{id}/{index}", produces = "image/jpeg")
    @ResponseBody
    public ResponseEntity<FileSystemResource> getImage(
            @PathVariable("id") Integer worksId,
            @PathVariable("index") Integer index
    ) {
        File path = FileTools.getFileMakePath(imagesPath, String.valueOf(worksId));
        File file = new File(path, index + ".jpg");
        return ResponseEntity.ok(new FileSystemResource(file));
    }

    @GetMapping(value = "/media/video/{id}", produces = "video/mp4")
    @ResponseBody
    public ResponseEntity<FileSystemResource> getVideo(@PathVariable("id") Integer id) {
        File path = FileTools.getFileMakePath(videosPath, String.valueOf(id));
        File file = new File(path, "video.mp4");
        return ResponseEntity.ok(new FileSystemResource(file));
    }

    @GetMapping(value = "/media/videocover/{id}",produces = "image/jpeg")
    @ResponseBody
    public ResponseEntity<FileSystemResource> getCover(@PathVariable("id") Integer id){
        File path = FileTools.getFileMakePath(videosPath, String.valueOf(id));
        File file = new File(path, "cover.jpg");
        return ResponseEntity.ok(new FileSystemResource(file));
    }
}
