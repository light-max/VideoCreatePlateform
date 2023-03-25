package com.lifengqiang.video.controller.api;

import com.lifengqiang.video.constant.GlobalConstant;
import com.lifengqiang.video.model.data.Result;
import com.lifengqiang.video.model.entity.User;
import com.lifengqiang.video.model.entity.Works;
import com.lifengqiang.video.model.result.WorksResult;
import com.lifengqiang.video.service.CollectService;
import com.lifengqiang.video.service.FollowService;
import com.lifengqiang.video.service.LikeService;
import com.lifengqiang.video.service.WorksService;
import com.lifengqiang.video.util.UseDefaultSuccessResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Controller
public class WorksController {
    @Resource
    WorksService service;

    @Resource
    LikeService likeService;

    @Resource
    CollectService collectService;

    @Resource
    FollowService followService;

    @PostMapping("/user/works/images")
    @ResponseBody
    @UseDefaultSuccessResponse
    public void submitImagesWorks(
            @SessionAttribute("user") User user,
            String content,
            MultipartFile[] files
    ) throws IOException {
        service.addImagesWorks(user.getId(), content, files);
    }

    @PostMapping("/user/works/video")
    @ResponseBody
    @UseDefaultSuccessResponse
    public void submitVideoWorks(
            @SessionAttribute("user") User user,
            String content,
            MultipartFile file,
            MultipartFile cover
    ) throws IOException {
        service.addVideoWorks(user.getId(), content, file, cover);
    }

    @GetMapping("/works/recommend/random/ids/reset")
    @ResponseBody
    @UseDefaultSuccessResponse
    public void resetRandomRecommendWorks(HttpSession session) {
        session.removeAttribute("recommend_random");
    }

    @GetMapping("/works/recommend/random/ids")
    @ResponseBody
    public Result<List<Integer>> getRandomRecommendWorks(HttpSession session) {
        Object object = session.getAttribute("recommend_random");
        Object object2 = session.getAttribute("user");
        User user = object2 != null ? (User) object2 : null;
        if (object == null) {
            object = service.getRandomWorks(user == null ? -1 : user.getId(), 100);
            session.setAttribute("recommend_random", object);
            return Result.success((List<Integer>) object);
        }
        List<Integer> ids = (List<Integer>) object;
        List<Integer> newIds = service.getRandomWorks(user == null ? -1 : user.getId(), 100);
        newIds.removeAll(ids);
        return Result.success(newIds);
    }

    @GetMapping("/works/details/{id}")
    @ResponseBody
    public Result<WorksResult> getDetails(
            @SessionAttribute(value = "user", required = false) User user,
            @PathVariable("id") Integer id
    ) {
        Works works = service.getById(id);
        GlobalConstant.dataNotExists.notNull(works);
        return Result.success(service.getResult(user == null ? -1 : user.getId(), works));
    }

    @GetMapping("/user/works/all/list")
    @ResponseBody
    public Result<List<WorksResult>> getUserWorksList(@SessionAttribute("user") User user) {
        return Result.success(service.getWorksByUserId(user.getId(), user.getId()));
    }

    @GetMapping({"/user/works/getbyuserid/{userId}", "/works/getbyuserid/{userId}"})
    @ResponseBody
    public Result<List<WorksResult>> getUserWorksListByUserId(
            @SessionAttribute(value = "user", required = false) User user,
            @PathVariable Integer userId
    ) {
        return Result.success(service.getWorksByUserId(user == null ? 0 : user.getId(), userId));
    }

    @GetMapping("/user/works/like/all/list")
    @ResponseBody
    public Result<List<WorksResult>> getUserLikeWorksList(@SessionAttribute("user") User user) {
        List<Integer> worksIds = likeService.getLikeWorksIdList(user.getId());
        Collections.reverse(worksIds);
        List<Works> works = service.listByIds(worksIds);
        List<WorksResult> results = service.getResultList(user.getId(), works);
        return Result.success(results);
    }

    @GetMapping("/user/works/collect/all/list")
    @ResponseBody
    public Result<List<WorksResult>> getUserCollectWorksList(@SessionAttribute("user") User user) {
        List<Integer> worksIds = collectService.getCollectWorksIdList(user.getId());
        Collections.reverse(worksIds);
        List<Works> works = service.listByIds(worksIds);
        List<WorksResult> results = service.getResultList(user.getId(), works);
        return Result.success(results);
    }

    @GetMapping("/user/works/id/follow/all/list")
    @ResponseBody
    public Result<List<Integer>> getUserFollowWorksIdList(@SessionAttribute("user") User user) {
        List<Integer> worksIds = followService.getFollowWorksIdList(user.getId());
        Collections.reverse(worksIds);
        return Result.success(worksIds);
    }

    @GetMapping("/user/works/follow/all/list")
    @ResponseBody
    public Result<List<WorksResult>> getUserFollowWorksList(@SessionAttribute("user") User user) {
        List<Integer> worksIds = followService.getFollowWorksIdList(user.getId());
        Collections.reverse(worksIds);
        List<Works> works = service.listByIds(worksIds);
        List<WorksResult> results = service.getResultList(user.getId(), works);
        return Result.success(results);
    }

    @GetMapping("/user/works/friend/all/list")
    @ResponseBody
    public Result<List<WorksResult>> getFriendWorksList(@SessionAttribute("user") User user) {
        List<Works> works = followService.getFriendWorksIdList(user.getId());
        List<WorksResult> results = service.getResultList(user.getId(), works);
        return Result.success(results);
    }

    @GetMapping("/api/works/byuserid")
    @ResponseBody
    public Result<List<WorksResult>> getUserWorksList(@SessionAttribute(value = "user", required = false) User user, Integer userId) {
        int currentUserId = user == null ? 0 : user.getId();
        List<WorksResult> results = service.getWorksByUserId(currentUserId, userId);
        return Result.success(results);
    }
}
