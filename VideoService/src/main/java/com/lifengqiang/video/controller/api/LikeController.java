package com.lifengqiang.video.controller.api;

import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lifengqiang.video.model.data.PagerData;
import com.lifengqiang.video.model.data.Result;
import com.lifengqiang.video.model.entity.Like;
import com.lifengqiang.video.model.entity.User;
import com.lifengqiang.video.model.result.LikeState;
import com.lifengqiang.video.service.LikeService;
import com.lifengqiang.video.service.WorksService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Controller
public class LikeController {
    @Resource
    LikeService service;

    @Resource
    WorksService worksService;

    @GetMapping("/user/like/works/{targetId}")
    @ResponseBody
    public Result<LikeState> checkWorks(
            @SessionAttribute(value = "user", required = false) User user,
            @PathVariable Integer targetId
    ) {
        LikeState state = LikeState.builder()
                .count(service.getCount(targetId, LikeService.WORKS))
                .type(LikeService.WORKS)
                .targetId(targetId)
                .userId(user == null ? -1 : user.getId())
                .like(user != null && service.isLike(user.getId(), targetId, LikeService.WORKS))
                .build();
        return Result.success(state);
    }

    @PostMapping("/user/like/works/{targetId}")
    @ResponseBody
    public Result<LikeState> toggleWorks(
            @SessionAttribute(value = "user") User user,
            @PathVariable Integer targetId) {
        boolean checked = service.toggle(user.getId(), targetId, LikeService.WORKS);
        LikeState state = LikeState.builder()
                .count(service.getCount(targetId, LikeService.WORKS))
                .type(LikeService.WORKS)
                .targetId(targetId)
                .userId(user.getId())
                .like(checked)
                .build();
        return Result.success(state);
    }

//    @GetMapping("/user/liked/works/list")
//    @ResponseBody
//    public PagerData getLikedWorks(@SessionAttribute("user") User user) {
//        service.page(new Page<Like>())
//    }

    @GetMapping("/user/like/remark/{targetId}")
    @ResponseBody
    public Result<LikeState> checkRemark(
            @SessionAttribute(value = "user", required = false) User user,
            @PathVariable Integer targetId
    ) {
        LikeState state = LikeState.builder()
                .count(service.getCount(targetId, LikeService.REMARK))
                .type(LikeService.REMARK)
                .targetId(targetId)
                .userId(user == null ? -1 : user.getId())
                .like(user != null && service.isLike(user.getId(), targetId, LikeService.REMARK))
                .build();
        return Result.success(state);
    }

    @PostMapping("/user/like/remark/{targetId}")
    @ResponseBody
    public Result<LikeState> toggleRemark(
            @SessionAttribute(value = "user") User user,
            @PathVariable Integer targetId) {
        boolean checked = service.toggle(user.getId(), targetId, LikeService.REMARK);
        LikeState state = LikeState.builder()
                .count(service.getCount(targetId, LikeService.REMARK))
                .type(LikeService.REMARK)
                .targetId(targetId)
                .userId(user.getId())
                .like(checked)
                .build();
        return Result.success(state);
    }
}
