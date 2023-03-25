package com.lifengqiang.video.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lifengqiang.video.mapper.UserMapper;
import com.lifengqiang.video.mapper.WorksMapper;
import com.lifengqiang.video.model.entity.User;
import com.lifengqiang.video.model.entity.Works;
import com.lifengqiang.video.model.result.WorksResult;
import com.lifengqiang.video.service.*;
import com.lifengqiang.video.util.FileTools;
import com.lifengqiang.video.util.datetranslate.DateFormatUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Service
public class WorksServiceImpl extends ServiceImpl<WorksMapper, Works> implements WorksService {
    @Value("${file.works.images}")
    private String imagesPath;

    @Value("${file.works.videos}")
    private String videosPath;

    @Resource
    UserMapper userMapper;

    @Resource
    LikeService likeService;

    @Resource
    CollectService collectService;

    @Resource
    RemarkService remarkService;

    @Resource
    FollowService followService;

    @Override
    public WorksResult getResult(int currentUserId, Works works) {
        User user = userMapper.selectById(works.getUserId());
        if (user == null) {
            user = User.builder().
                    username("").
                    nickname("").
                    build();
        }
        List<String> images = null;
        String video = null;
        String cover;
        if (works.getType() == 0) {
            images = new ArrayList<>();
            File path = FileTools.getFileMakePath(imagesPath, String.valueOf(works.getId()));
            File[] files = path.listFiles();
            for (File file : Objects.requireNonNull(files)) {
                String name = file.getName().replace(".jpg", "");
                images.add(String.format("/media/image/%d/%s", works.getId(), name));
            }
            cover = images.get(0);
        } else {
            video = "/media/video/" + works.getId();
            cover = "/media/videocover/" + works.getId();
        }
        return WorksResult.builder()
                .id(works.getId())
                .userId(works.getUserId())
                .type(works.getType())
                .content(works.getContent())
                .createTime(works.getCreateTime())
                .date(DateFormatUtil.format(works.getCreateTime()))
                .username(user.getUsername())
                .nickname(user.getNickname())
                .userhead("/head/" + works.getUserId())
                .images(images)
                .video(video)
                .cover(cover)
                .like(likeService.isLike(currentUserId, works.getId(), LikeService.WORKS))
                .likeCount(likeService.getCount(works.getId(), LikeService.WORKS))
                .collect(collectService.isCollect(currentUserId, works.getId()))
                .collectCount(collectService.getCount(works.getId()))
                .remarkCount(remarkService.getRemarkCount(works.getId()))
                .follwuser(followService.isFollow(currentUserId, works.getUserId()))
                .build();
    }

    @Override
    public List<WorksResult> getResultList(int currentUserId, List<Works> works) {
        List<WorksResult> results = new ArrayList<>();
        for (Works work : works) {
            results.add(getResult(currentUserId, work));
        }
        return results;
    }

    @Override
    public void addImagesWorks(int userId, String content, MultipartFile[] files) throws IOException {
        Works works = Works.builder()
                .userId(userId)
                .type(0)
                .content(content)
                .build()
                .check();
        save(works);
        File path = FileTools.getFileMakePath(imagesPath, Integer.toString(works.getId()));
        if (!path.exists()) {
            path.mkdir();
        }
        FileTools.deleteChildFiles(path);
        for (int i = 0; i < files.length; i++) {
            File saveFile = new File(path, (i + 1) + ".jpg");
            InputStream in = files[i].getInputStream();
            FileTools.write(in, saveFile);
        }
    }

    @Override
    public void addVideoWorks(int userId, String content, MultipartFile file, MultipartFile cover) throws IOException {
        Works works = Works.builder()
                .userId(userId)
                .type(1)
                .content(content)
                .build()
                .check();
        save(works);
        File path = FileTools.getFileMakePath(videosPath, Integer.toString(works.getId()));
        if (!path.exists()) {
            path.mkdir();
        }
        File saveFile = new File(path, "video.mp4");
        FileTools.write(file.getInputStream(), saveFile);
        File coverFile = new File(path, "cover.jpg");
        FileTools.write(cover.getInputStream(), coverFile);
    }

    @Override
    public List<Integer> getRandomWorks(int currentUserId, int size) {
        int count = count();
        List<Integer> ids;
        Random r = new Random();
        if (size > count) {
            ids = baseMapper.getAllIds();
        } else {
            int limitMax = count - size;
            int index = r.nextInt(limitMax);
            ids = baseMapper.getIds(index, size);
        }
        List<Integer> result = new ArrayList<>();
        while (!ids.isEmpty()) {
            int index = r.nextInt(ids.size());
            result.add(ids.remove(index));
        }
        return result;
    }

    @Override
    public List<WorksResult> getWorksByUserId(int currentUserId, int userId) {
        List<Works> list = list(new QueryWrapper<Works>()
                .lambda()
                .orderByDesc(Works::getCreateTime)
                .eq(Works::getUserId, userId));
        return getResultList(userId, list);
    }
}
