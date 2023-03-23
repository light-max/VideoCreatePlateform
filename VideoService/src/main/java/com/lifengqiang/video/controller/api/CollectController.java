package com.lifengqiang.video.controller.api;

import com.lifengqiang.video.model.data.Result;
import com.lifengqiang.video.model.entity.User;
import com.lifengqiang.video.model.result.CollectState;
import com.lifengqiang.video.model.result.LikeState;
import com.lifengqiang.video.service.CollectService;
import com.lifengqiang.video.service.LikeService;
import com.lifengqiang.video.service.WorksService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Controller
public class CollectController {
    @Resource
    CollectService service;

    @Resource
    WorksService worksService;

    @GetMapping("/user/collect/{worksId}")
    @ResponseBody
    public Result<CollectState> check(
            @SessionAttribute(value = "user", required = false) User user,
            @PathVariable Integer worksId
    ) {
        CollectState state = CollectState.builder()
                .count(service.getCount(worksId))
                .worksId(worksId)
                .userId(user == null ? -1 : user.getId())
                .collect(user != null && service.isCollect(user.getId(), worksId))
                .build();
        return Result.success(state);
    }

    @PostMapping("/user/collect/{worksId}")
    @ResponseBody
    public Result<CollectState> toggle(
            @SessionAttribute("user") User user,
            @PathVariable Integer worksId
    ) {
        boolean checked = service.toggle(user.getId(), worksId);
        CollectState state = CollectState.builder()
                .count(service.getCount(worksId))
                .worksId(worksId)
                .userId(user.getId())
                .collect(checked)
                .build();
        return Result.success(state);
    }
}
